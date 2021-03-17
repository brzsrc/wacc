package backend.intel;


import backend.Instruction;
import backend.InstructionGenerator;
import backend.arm.instructions.BL;
import backend.arm.instructions.LDR;
import backend.arm.instructions.STR;
import backend.arm.instructions.STR.StrMode;
import backend.arm.instructions.addressing.AddressingMode2;
import backend.arm.instructions.addressing.ImmedAddress;
import backend.arm.instructions.addressing.Operand2;
import backend.intel.instructions.*;
import backend.intel.instructions.Mov.IntelMovType;
import backend.intel.instructions.address.IntelAddress;
import backend.intel.instructions.arithmetic.Add;
import backend.intel.instructions.arithmetic.IntelArithmeticLogic;
import backend.intel.instructions.arithmetic.Sal;
import backend.intel.instructions.arithmetic.Sub;
import backend.intel.instructions.directives.CFIEndProc;
import backend.intel.instructions.directives.CFIStartProc;
import frontend.node.FuncNode;
import frontend.node.ProgramNode;
import frontend.node.StructDeclareNode;
import frontend.node.expr.ArrayElemNode;
import frontend.node.expr.ArrayNode;
import frontend.node.expr.BinopNode;
import frontend.node.expr.BinopNode.Binop;
import frontend.node.expr.BoolNode;
import frontend.node.expr.CharNode;
import frontend.node.expr.ExprNode;
import frontend.node.expr.FunctionCallNode;
import frontend.node.expr.IdentNode;
import frontend.node.expr.IntegerNode;
import frontend.node.expr.PairElemNode;
import frontend.node.expr.PairNode;
import frontend.node.expr.StringNode;
import frontend.node.expr.StructElemNode;
import frontend.node.expr.StructNode;
import frontend.node.expr.UnopNode;
import frontend.node.expr.UnopNode.Unop;
import frontend.node.stat.AssignNode;
import frontend.node.stat.DeclareNode;
import frontend.node.stat.ExitNode;
import frontend.node.stat.ForNode;
import frontend.node.stat.FreeNode;
import frontend.node.stat.IfNode;
import frontend.node.stat.JumpNode;
import frontend.node.stat.PrintNode;
import frontend.node.stat.PrintlnNode;
import frontend.node.stat.ReadNode;
import frontend.node.stat.ReturnNode;
import frontend.node.stat.ScopeNode;
import frontend.node.stat.SkipNode;
import frontend.node.stat.StatNode;
import frontend.node.stat.SwitchNode;
import frontend.node.stat.WhileNode;
import frontend.type.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import utils.Utils.IntelInstructionSize;
import utils.backend.LabelGenerator;
import utils.backend.register.Register;
import utils.backend.register.intel.IntelConcreteRegister;
import utils.backend.register.intel.IntelConcreteRegisterAllocator;

import static backend.arm.instructions.LDR.LdrMode.LDRSB;
import static backend.arm.instructions.STR.StrMode.STR;
import static backend.arm.instructions.STR.StrMode.STRB;
import static backend.arm.instructions.addressing.AddressingMode2.AddrMode2.OFFSET;
import static backend.arm.instructions.addressing.AddressingMode2.AddrMode2.PREINDEX;
import static backend.arm.instructions.arithmeticLogic.ARMArithmeticLogic.armUnopAsm;
import static utils.Utils.ARM_POINTER_SIZE;
import static utils.Utils.BOOL_BASIC_TYPE;
import static utils.Utils.CHAR_ARRAY_TYPE;
import static utils.Utils.CHAR_BASIC_TYPE;
import static utils.Utils.FALSE;
import static utils.Utils.FUNC_HEADER;
import static utils.Utils.INTEL_POINTER_SIZE;
import static utils.Utils.INT_BASIC_TYPE;
import static utils.Utils.RoutineInstruction.CHECK_NULL_POINTER;
import static utils.Utils.STRING_BASIC_TYPE;
import static utils.Utils.SystemCallInstruction.MALLOC;
import static utils.Utils.TRUE;
import static utils.Utils.WORD_SIZE;
import static utils.Utils.intToIntelSize;
import static utils.backend.Cond.E;
import static utils.backend.Cond.EQ;
import static utils.backend.Cond.NE;
import static utils.backend.Cond.NULL;
import static utils.backend.register.arm.ARMConcreteRegister.PC;
import static utils.backend.register.arm.ARMConcreteRegister.SP;
import static utils.backend.register.arm.ARMConcreteRegister.r0;
import static utils.backend.register.intel.IntelConcreteRegister.*;

public class IntelInstructionGenerator extends InstructionGenerator<IntelInstruction> {

  /* label generators for data section and branches */
  private final LabelGenerator<Label> branchLabelGenerator;
  private final LabelGenerator<Label> dataLabelGenerator;

  /* intel regsiter allocator */
  private final IntelConcreteRegisterAllocator intelRegAllocator;

  /* the data section in the assembly */
  private final Map<Label, String> dataSection;
  private final Map<String, Label> biDataSection;

  private int currParamListSize;

  public IntelInstructionGenerator() {
    branchLabelGenerator = new LabelGenerator<>(".L", Label.class);
    dataLabelGenerator = new LabelGenerator<>(".LC", Label.class);
    intelRegAllocator = new IntelConcreteRegisterAllocator();
    dataSection = new LinkedHashMap<>();
    biDataSection = new LinkedHashMap<>();
    currParamListSize = 0;
  }

  @Override
  public Void visitArrayElemNode(ArrayElemNode node) {
    /* get the address of this array and store it in an available register */
    IntelConcreteRegister addrReg = intelRegAllocator.allocate();

    int offset = currSymbolTable.getStackOffset(node.getName(), node.getSymbol())
        + stackOffset;
    instructions.add(new Mov(new IntelAddress(rbp, -offset), addrReg));

    IntelConcreteRegister indexReg;
    for (int i = 0; i < node.getDepth(); i++) {
      /* load the index at depth `i` to the next available register */
      ExprNode index = node.getIndex().get(i);
      if (!(index instanceof IntegerNode)) {
        visit(index);
        indexReg = intelRegAllocator.curr();
        if (isLhs) {
          instructions.add(new Mov(new IntelAddress(indexReg), indexReg.withSize(intToIntelSize.get(index.getType().getSize()))));
        }
      } else {
        indexReg = intelRegAllocator.allocate();
        instructions.add(new Mov(new IntelAddress(((IntegerNode) index).getVal()), indexReg));
      }

      int elemSize = i < node.getDepth() - 1 ? 3 : node.getType().getSize() / 2;
      instructions.add(new Sal(elemSize, IntelInstructionSize.Q, indexReg));
      instructions.add(new Add(indexReg, addrReg));
      if (i < node.getDepth() - 1) instructions.add(new Mov(new IntelAddress(addrReg), addrReg));

      /* free indexReg to make it available for the indexing of the next depth */
      intelRegAllocator.free();
    }

    /* if is not lhs, load the array content to `reg` */
    if (!isLhs) {
      instructions.add(new Mov(new IntelAddress(addrReg), addrReg));
    }

    return null;
  }

  @Override
  public Void visitArrayNode(ArrayNode node) {
    /* get the total number of bytes needed to allocate enough space for the array */
    int size = node.getType() == null ? 0 : node.getContentSize() * node.getLength();

    /* load edi with the number of bytes needed and malloc */
    instructions.add(new Mov(new IntelAddress(size), rdi.withSize(IntelInstructionSize.L)));
    instructions.add(new Call("malloc@PLT"));

    /* then MOV the result pointer to another register */
    IntelConcreteRegister addrReg = intelRegAllocator.allocate();
    instructions.add(new Mov(rax, addrReg));

    for (int i = 0; i < node.getLength(); i++) {
      visit(node.getElem(i));
      int indexOffset = node.getContentSize() * i;
      IntelConcreteRegister tempAddr = intelRegAllocator.allocate();
      instructions.add(new Mov(addrReg, tempAddr));
      instructions.add(new Add(indexOffset, IntelInstructionSize.Q, tempAddr));
      instructions.add(new Mov(intelRegAllocator.last().withSize(intToIntelSize.get(node.getContentSize()))
          , new IntelAddress(tempAddr)));
      intelRegAllocator.free();
      intelRegAllocator.free();
    }

    return null;
  }

  @Override
  public Void visitBinopNode(BinopNode node) {
    ExprNode expr1 = node.getExpr1();
    ExprNode expr2 = node.getExpr2();
    Register e1reg, e2reg;

    Binop operator = node.getOperator();
    Type type = expr1.getType();
    IntelInstructionSize size = intToIntelSize.get(type.getSize());

    /* potential optimise here */
    if (expr1.getWeight() >= expr2.getWeight()) {
      visit(expr1);
      visit(expr2);
      e2reg = intelRegAllocator.curr();
      e1reg = intelRegAllocator.last();

      instructions.addAll(IntelArithmeticLogic.intelBinopAsm
          .binopAssemble(e1reg.asIntelRegister().withSize(size),
              e2reg.asIntelRegister().withSize(size), null, operator)
          .stream().map(i -> (IntelInstruction) i).collect(Collectors.toList()));
    } else {
      visit(expr2);
      visit(expr1);
      e2reg = intelRegAllocator.last();
      e1reg = intelRegAllocator.curr();

      instructions.addAll(IntelArithmeticLogic.intelBinopAsm
          .binopAssemble(e1reg.asIntelRegister().withSize(size),
              e2reg.asIntelRegister().withSize(size), null, operator)
          .stream().map(i -> (IntelInstruction) i).collect(Collectors.toList()));
      instructions.add(new Mov(e1reg, e2reg));
    }

    if (operator.equals(Binop.DIV)) {
      instructions.add(new Mov(rax.withSize(IntelInstructionSize.L), e1reg.asIntelRegister().withSize(IntelInstructionSize.L)));
    } else if (operator.equals(Binop.MOD)) {
      instructions.add(new Mov(rdx.withSize(IntelInstructionSize.L), e1reg.asIntelRegister().withSize(IntelInstructionSize.L)));
    }

    if (expr1.getWeight() < expr2.getWeight()) {
      instructions.add(new Mov(e1reg, e2reg));
    }
    intelRegAllocator.free();

    return null;
  }

  @Override
  public Void visitBoolNode(BoolNode node) {
    IntelConcreteRegister reg = intelRegAllocator.allocate();
    int val = node.getVal() ? TRUE : FALSE;
    instructions.add(new Mov(new IntelAddress(val), reg));
    return null;
  }

  @Override
  public Void visitCharNode(CharNode node) {
    IntelConcreteRegister reg = intelRegAllocator.allocate();
    int val = node.getAsciiValue();
    instructions.add(new Mov(new IntelAddress(val), reg));
    return null;
  }

  @Override
  public Void visitFunctionCallNode(FunctionCallNode node) {

    /*
     * 1 compute parameters, all parameter in stack also add into function's
     * identmap
     */
    List<ExprNode> params = node.getParams();
    int paramSize = 0;
    stackOffset = 0;
    int paramNum = params.size();

    /* assign rbp as rsp, so that new parameters added will not overwrite variables */
    // todo: 1 mov should be consistent with moved in parameter size,
    //       2 should move to stack below rsp, prevent overwrite variable in using by main body

    for (int i = paramNum - 1; i >= 0; i--) {
      ExprNode expr = params.get(i);
      visit(expr);
      IntelConcreteRegister reg = intelRegAllocator.curr();
      int size = expr.getType().getSize();
      instructions.add(new Sub(size, IntelInstructionSize.Q, rbp));
      instructions.add(new Mov(reg.withSize(intToIntelSize.get(size)), new IntelAddress(rbp)));

      intelRegAllocator.free();

      paramSize += size;
      stackOffset += size;
    }
    stackOffset = 0;
    
    /* 2 call function with B instruction */
    instructions.add(new Call(FUNC_HEADER + node.getFunction().getFunctionName()));

    /* 3 add back stack pointer */
    if (paramSize > 0) {
      instructions.add(new Add(paramSize, IntelInstructionSize.Q, rbp));
    }

    /* 4 get result, put in a general register */
    instructions.add(new Mov(rax, intelRegAllocator.allocate()));
    return null;
  }

  @Override
  public Void visitIdentNode(IdentNode node) {
    int identTypeSize = node.getType().getSize();

    /* put pointer that point to ident's value in stack to next available register */
    System.out.println("ident " + node.getName() + " has:");
    System.out.println("offset " + currSymbolTable.getStackOffset(node.getName(), node.getSymbol()));
    System.out.println("paramList size " + currParamListSize);
    System.out.println("stack offset" + stackOffset);
    System.out.println();
    int offset = currSymbolTable.getStackOffset(node.getName(), node.getSymbol())
        - currParamListSize + stackOffset;

    /* if is lhs, then only put address in register */
    if (isLhs) {
      instructions.add(new Lea(new IntelAddress(rbp, -offset), intelRegAllocator.allocate()));
    } else {
      /* otherwise, put value in register */
      Map<Integer, IntelMovType> m = Map.of(8, IntelMovType.MOV, 4, IntelMovType.MOV, 1, IntelMovType.MOVZBQ);
      IntelMovType type = m.get(identTypeSize);
      IntelInstructionSize size = type.equals(IntelMovType.MOV) ? intToIntelSize.get(identTypeSize) : IntelInstructionSize.Q;
      instructions.add(new Mov(new IntelAddress(rbp, -offset), intelRegAllocator.allocate().withSize(size), type));
    }
    return null;
  }

  @Override
  public Void visitIntegerNode(IntegerNode node) {
    IntelConcreteRegister reg = intelRegAllocator.allocate();
    instructions.add(new Mov(new IntelAddress(node.getVal()), reg));
    return null;
  }

  @Override
  public Void visitPairElemNode(PairElemNode node) {
    /* 1 get pointer to the pair from stack
     *   store into next available register
     *   reg is expected register where visit will put value in */

    /* e.g. read fst a, (fst a) is used as lhs but (a) is used as rhs */
    Register reg = intelRegAllocator.next();
    boolean isLhsOutside = isLhs;
    int size = node.getPair().getType().getSize();
    isLhs = false;
    visit(node.getPair());
    isLhs = isLhsOutside;

    /* 2 get pointer to child
     *   store in the same register, save register space
     *   no need to check whether child has initialised, as it is in lhs */
    IntelAddress addr;

    if (node.isFirst()) {
      addr = new IntelAddress(reg.asIntelRegister());
    } else {
      addr = new IntelAddress(reg.asIntelRegister(), INTEL_POINTER_SIZE);
    }

    instructions.add(new Mov(addr, reg));

    if (!isLhs) {
      instructions.add(new Mov(new IntelAddress(reg.asIntelRegister()), reg.asIntelRegister().withSize(intToIntelSize.get(size))));
    }
    return null;
  }

  @Override
  public Void visitPairNode(PairNode node) {
    /* null is also a pairNode
     *  if one of child is null, the other has to be null */
    if (node.getFst() == null || node.getSnd() == null) {
      instructions.add(new Mov(new IntelAddress(0), intelRegAllocator.allocate()));
      return null;
    }

    /* 1 malloc pair */
    /* 1.1 move size of a pair in r0
     *    pair in heap is 2 pointers, so 8 byte */
    instructions.add(new Mov(new IntelAddress(2 * INTEL_POINTER_SIZE), rdi));

    /* 1.2 BL malloc and get pointer in general use register*/
    instructions.add(new Call("malloc@PLT"));
    Register pairPointer = intelRegAllocator.allocate();

    instructions.add(new Mov(rax, pairPointer));

    /* 2 visit both child */
    visitPairChildExpr(node.getFst(), pairPointer, 0);
    /* pair contains two pointers, each with size 4 */
    visitPairChildExpr(node.getSnd(), pairPointer, INTEL_POINTER_SIZE);
    return null;
  }

  private void visitPairChildExpr(ExprNode child, Register pairPointer, int offset) {
    int size = child.getType().getSize();

    /* 1 move size of fst child in r0 */
    instructions.add(new Mov(new IntelAddress(size), rdi));

    /* 2 BL malloc, assign child value and get pointer in heap area pairPointer[0] or [1] */
    instructions.add(new Call("malloc@PLT"));

    /* 3 visit fst expression, get result in general register */
    visit(child);
    Register fstVal = intelRegAllocator.curr();

    instructions.add(new Mov(fstVal.asIntelRegister().withSize(intToIntelSize.get(size)), new IntelAddress(rax)));
    instructions.add(new Mov(rax, new IntelAddress(pairPointer.asIntelRegister(), offset)));

    /* free register used for storing child's value */
    intelRegAllocator.free();
  }

  @Override
  public Void visitStringNode(StringNode node) {
    /* Add msg into the data list */
    String strWithQuote = node.getString();
    String str = strWithQuote.substring(1, strWithQuote.length() - 1);
    Label msgLabel = dataLabelGenerator.getLabel().asIntelLabel();
    dataSection.put(msgLabel, str);
    biDataSection.put(str, msgLabel);

    IntelConcreteRegister reg = intelRegAllocator.allocate();

    instructions.add(new Lea(new IntelAddress(rip, msgLabel), reg));
    return null;
  }

  @Override
  public Void visitUnopNode(UnopNode node) {
    visit(node.getExpr());

    Register reg = intelRegAllocator.curr();
    Unop operator = node.getOperator();

    List<Instruction> insList = IntelArithmeticLogic.intelUnopAsm
        .unopAssemble(reg, reg, operator, node.getExpr());
    instructions.addAll(insList.stream().map(i -> (IntelInstruction) i).collect(Collectors.toList()));

    return null;
  }

  @Override
  public Void visitAssignNode(AssignNode node) {
    /* visit rhs */
    visit(node.getRhs());

    /* visit lhs */
    isLhs = true;
    visit(node.getLhs());
    isLhs = false;

    int size = node.getLhs().getType().getSize();

    IntelConcreteRegister reg = intelRegAllocator.last();

    instructions.add(new Mov(reg.withSize(intToIntelSize.get(size)), new IntelAddress(intelRegAllocator.curr())));
    intelRegAllocator.free();
    intelRegAllocator.free();
    return null;
  }

  @Override
  public Void visitDeclareNode(DeclareNode node) {
    visit(node.getRhs());
    int identTypeSize = node.getRhs().getType().getSize();

    /* TODO: add intel move type here */

    int offset = node.getScope().lookup(node.getIdentifier()).getStackOffset();

    instructions.add(new Mov(intelRegAllocator.curr().withSize(intToIntelSize.get(identTypeSize)), new IntelAddress(rbp, -offset)));
    intelRegAllocator.free();
    return null;
  }

  @Override
  public Void visitExitNode(ExitNode node) {
    /* If regs were allocated correctly for every statement
     * then the argument value of exit would be put into r4 */
    visit(node.getValue());
    /* Mov the argument value from r4 to r0 */
    instructions.add(new Mov(intelRegAllocator.curr(), rdi));
    /* Call the exit function */
    instructions.add(new Call("exit@PLT"));

    return null;
  }

  @Override
  public Void visitFreeNode(FreeNode node) {
    visit(node.getExpr());
    instructions.add(new Mov(intelRegAllocator.curr(), rdi));
    instructions.add(new Call("free@PLT"));
    intelRegAllocator.free();

    return null;
  }

  @Override
  public Void visitIfNode(IfNode node) {
    Label elseIfLabel = branchLabelGenerator.getLabel().asIntelLabel();
    Label exitLabel = branchLabelGenerator.getLabel().asIntelLabel();

    /* 1 condition check, branch */
    visit(node.getCond());
    IntelConcreteRegister cond = intelRegAllocator.curr();
    IntelConcreteRegister oneReg = intelRegAllocator.allocate();
    instructions.add(new Mov(new IntelAddress(1), oneReg));
    instructions.add(new Cmp(cond.withSize(IntelInstructionSize.B), oneReg.withSize(IntelInstructionSize.B)));
    instructions.add(new Jmp(NE, elseIfLabel.getName()));
    intelRegAllocator.free();
    intelRegAllocator.free();

    /* translate if body */
    visit(node.getIfBody());
    instructions.add(new Jmp(exitLabel.getName()));

    instructions.add(elseIfLabel);
    visit(node.getElseBody());

    /* 3 end of if statement */
    instructions.add(exitLabel);

    return null;
  }

  @Override
  public Void visitPrintlnNode(PrintlnNode node) {
    /* print content same as printNode */
    visitPrintNode(new PrintNode(node.getExpr()));

    Label newLineLabel = biDataSection.get("\\n");
    if (!alreadyExist("\\n")) {
      newLineLabel = dataLabelGenerator.getLabel();
      dataSection.put(newLineLabel, "\\n");
      biDataSection.put("\\n", newLineLabel);
    }

    instructions.add(new Lea(new IntelAddress(rip, newLineLabel), rdi));
    instructions.add(new Mov(new IntelAddress(0), rax));
    instructions.add(new Call("printf@PLT"));

    return null;
  }

  private boolean alreadyExist(String str) {
    return dataSection.values().contains(str);
  }

  private final Map<Type, String> printTypeStringMap = Map.of(
      INT_BASIC_TYPE,    "%d",
      CHAR_BASIC_TYPE,   "%c",
      STRING_BASIC_TYPE, "%s",
      CHAR_ARRAY_TYPE,   "%s"
  );

  @Override
  public Void visitPrintNode(PrintNode node) {
    visit(node.getExpr());

    Type type = node.getExpr().getType();

    String printTypeString = printTypeStringMap.get(type);

    if (type.equals(BOOL_BASIC_TYPE)) {
      Label trueMsgLabel = biDataSection.get("true");
      Label falseMsgLabel = biDataSection.get("false");
      if (!alreadyExist("true")) {
        trueMsgLabel = dataLabelGenerator.getLabel();
        falseMsgLabel = dataLabelGenerator.getLabel();
        dataSection.put(trueMsgLabel, "true");
        biDataSection.put("true", trueMsgLabel);
        dataSection.put(falseMsgLabel, "false");
        biDataSection.put("false", falseMsgLabel);
      }
        Label trueLabel = branchLabelGenerator.getLabel();
        Label falseLabel = branchLabelGenerator.getLabel();
        Label afterLabel = branchLabelGenerator.getLabel();
        IntelConcreteRegister oneReg = intelRegAllocator.allocate();
        instructions.add(new Mov(new IntelAddress(1), oneReg));
        instructions.add(new Cmp(intelRegAllocator.last().withSize(IntelInstructionSize.B), oneReg.withSize(IntelInstructionSize.B)));
        intelRegAllocator.free();
        instructions.add(new Jmp(E, trueLabel.getName()));
        instructions.add(new Jmp(NULL, falseLabel.getName()));
        instructions.add(trueLabel);
        instructions.add(new Lea(new IntelAddress(rip, trueMsgLabel), rdi));
        instructions.add(new Jmp(NULL, afterLabel.getName()));
        instructions.add(falseLabel);
        instructions.add(new Lea(new IntelAddress(rip, falseMsgLabel), rdi));
        instructions.add(new Jmp(NULL, afterLabel.getName()));
        instructions.add(afterLabel);
    } else {
      Label printLabel = biDataSection.get(printTypeString);
      if (!alreadyExist(printTypeString)) {
        printLabel = dataLabelGenerator.getLabel();
        dataSection.put(printLabel, printTypeString);
        biDataSection.put(printTypeString, printLabel);
      }
      instructions.add(new Mov(intelRegAllocator.curr(), rsi));
      instructions.add(new Lea(new IntelAddress(rip, printLabel), rdi));
    }

    instructions.add(new Mov(new IntelAddress(0), rax));
    instructions.add(new Call("printf@PLT"));

    intelRegAllocator.free();

    return null;
  }

  @Override
  public Void visitReadNode(ReadNode node) {
    /* visit the expr first, treat it as left-hand side expr so that we get its address instead of value */
    isLhs = true;
    visit(node.getInputExpr());
    isLhs = false;

    /* get the type of expr to determine whether we need to read an int or a char */
    Type type = node.getInputExpr().getType();

    Label l = dataLabelGenerator.getLabel();
    if (type.equalToType(INT_BASIC_TYPE)) {
      dataSection.put(l, "%d");
      biDataSection.put("%d", l);
    } else if (type.equalToType(CHAR_BASIC_TYPE)) {
      dataSection.put(l, "%c");
      biDataSection.put("%c", l);
    }

    instructions.add(new Mov(intelRegAllocator.curr(), rsi));
    instructions.add(new Lea(new IntelAddress(rip, l), rdi));
    /* choose between read_int and read_char */
    instructions.add(new Mov(new IntelAddress(0), rax));
    instructions.add(new Call("__isoc99_scanf@PLT"));

    intelRegAllocator.free();

    return null;
  }

  @Override
  public Void visitReturnNode(ReturnNode node) {
    visit(node.getExpr());
    instructions.add(new Mov(intelRegAllocator.curr(), rax));
    intelRegAllocator.free();
    if (funcStackSize != 0) {
      instructions.add(new Add(funcStackSize, IntelInstructionSize.Q, rsp));
    }
    instructions.add(new Pop(rbp));
    instructions.add(new Ret());
    return null;
  }

  @Override
  public Void visitScopeNode(ScopeNode node) {
    List<StatNode> list = node.getBody();

    /* 1 leave space for variables in stack */
    int stackSize = node.getScope().getParentSymbolTable() == null
        ? 0 : node.getScope().getParentSymbolTable().getSize();
    int temp = stackSize;
    while (temp > 0) {
      int realStackSize = temp / 1024 >= 1 ? 1024 : temp;
      instructions.add(new Sub(realStackSize, IntelInstructionSize.Q, rbp));
      temp = temp - realStackSize;
    }

    /* accumulate function stack size, in case this scope is a function scope and contain return */
    funcStackSize += stackSize;

    /* 2 visit statements
     *   set currentSymbolTable here, eliminate all other set symbol table in other statNode */
    currSymbolTable = node.getScope();
    for (StatNode elem : list) {
      visit(elem);
    }
    currSymbolTable = currSymbolTable.getParentSymbolTable();

    /* decrease function stack size, as from this point stack is freed by the scope, not by return */
    funcStackSize -= stackSize;

    /* 3 restore stack */
    temp = stackSize;
    while (temp > 0) {
      int realStackSize = temp / 1024 >= 1 ? 1024 : temp;
      instructions.add(new Add(realStackSize, IntelInstructionSize.Q, rbp));
      temp = temp - realStackSize;
    }

    return null;
  }

  @Override
  public Void visitSkipNode(SkipNode node) {
    return null;
  }

  @Override
  public Void visitWhileNode(WhileNode node) {
    /* 1 unconditional jump to end of loop, where conditional branch exists */
    /* if we encountere a do-while loop, then do not add the conditional jump */
    Label testLabel = branchLabelGenerator.getLabel().asIntelLabel();
    if (!node.isDoWhile()) {
      instructions.add(new Jmp(NULL, testLabel.getName()));
    }

    /* 2 get a label, mark the start of the loop */
    Label startLabel = branchLabelGenerator.getLabel().asIntelLabel();
    Label nextLabel = branchLabelGenerator.getLabel().asIntelLabel();

    /* restore the last jump-to label after visiting the while-loop body */
    Label lastBreakJumpToLabel = currBreakJumpToLabel == null ? null : currBreakJumpToLabel.asIntelLabel();
    Label lastContinueJumpToLabel = currContinueJumpToLabel == null ? null : currContinueJumpToLabel.asIntelLabel();
    currBreakJumpToLabel = nextLabel;
    currContinueJumpToLabel = testLabel;

    instructions.add(startLabel);

    /* 3 loop body */
    visit(node.getBody());

    currBreakJumpToLabel = lastBreakJumpToLabel;
    currContinueJumpToLabel = lastContinueJumpToLabel;

    /* 4 start of condition test */
    instructions.add(testLabel);
    /* translate cond expr */
    visit(node.getCond());

    IntelConcreteRegister oneReg = intelRegAllocator.allocate();
    instructions.add(new Mov(new IntelAddress(1), oneReg));
    instructions.add(new Cmp(intelRegAllocator.last().withSize(IntelInstructionSize.B), oneReg.withSize(IntelInstructionSize.B)));
    intelRegAllocator.free();

    /* 5 conditional branch jump to the start of loop */
    instructions.add(new Jmp(E, startLabel.getName()));

    instructions.add(nextLabel);

    intelRegAllocator.free();
    return null;
  }

  @Override
  public Void visitForNode(ForNode node) {
    return null;
  }

  @Override
  public Void visitJumpNode(JumpNode node) {
    return null;
  }

  @Override
  public Void visitSwitchNode(SwitchNode node) {
    return null;
  }

  @Override
  public Void visitFuncNode(FuncNode node) {
    /* cannot call get stack size on function body, as that will return 0
     * public field used here, so that on visit return statement, return can add stack back */
    funcStackSize = node.getFunctionBody().getScope().getSize();
    funcStackSize -= node.paramListStackSize();

    /* 1 add function label,
     *   PUSH {lr}
     */
    instructions.add(new Label(FUNC_HEADER + node.getFunctionName()));
    instructions.add(new Push(Collections.singletonList(rbp)));
    instructions.add(new Mov(rsp, rbp));

    /* 2 decrease stack, leave space for variable in function body
     *   DOES NOT include parameters' stack area */
    if (funcStackSize != 0) {
      instructions.add(new Sub(funcStackSize, IntelInstructionSize.Q, rsp));
    }

    /* 3 visit function,
     *   RETURN are responsible for adding stack back
     */
    currParamListSize = node.paramListStackSize();
    visit(node.getFunctionBody());
    currParamListSize = 0;

    /* function always add pop and ltorg at the end of function body */
//    instructions.add(new Pop(rbp));

    return null;
  }

  @Override
  public Void visitProgramNode(ProgramNode node) {
    /* 1 translate all functions */
    for (FuncNode func : node.getFunctions().values()) {
      visitFuncNode(func);
    }

    /* 2 start of main */
    Label mainLabel = new Label("main");
    instructions.add(mainLabel);
    instructions.add(new CFIStartProc());
    /* 3 PUSH rsp and MOV rsp to rbp */
    instructions.add(new Push(Collections.singletonList(rbp)));
    instructions.add(new Mov(rsp, rbp));
    instructions.add(new Sub(Math.max(16, node.getBody().getScope().getSize()), IntelInstructionSize.Q, rsp));

    /* 4 main body */
    visit(node.getBody());

    instructions.add(new Add(Math.max(16, node.getBody().getScope().getSize()), IntelInstructionSize.Q, rsp));
    /* 5 set return value and return */
    instructions.add(new Mov(new IntelAddress(0), rax.withSize(IntelInstructionSize.L)));
//    instructions.add(new Pop(rbp)); // delete by sx119: otherwise cannot pass functionSimple
    instructions.add(new Leave());
    instructions.add(new CFIEndProc());
    instructions.add(new Ret());
    return null;
  }

  @Override
  public Void visitStructElemNode(StructElemNode node) {
    return null;
  }

  @Override
  public Void visitStructNode(StructNode node) {
    return null;
  }

  @Override
  public Void visitStructDeclareNode(StructDeclareNode node) {
    return null;
  }

  public List<IntelInstruction> getInstructions() {
    return instructions;
  }

  public Map<Label, String> getDataSection() {
    return dataSection;
  }
}
