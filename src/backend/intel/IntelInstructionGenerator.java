package backend.intel;


import backend.Instruction;
import backend.InstructionGenerator;
import backend.arm.instructions.ARMInstruction;
import backend.arm.instructions.BL;
import backend.arm.instructions.STR;
import backend.arm.instructions.STR.StrMode;
import backend.arm.instructions.addressing.AddressingMode2;
import backend.arm.instructions.addressing.Operand2;
import backend.common.address.Address;
import backend.intel.instructions.Call;
import backend.intel.instructions.IntelInstruction;
import backend.intel.instructions.Label;
import backend.intel.instructions.Lea;
import backend.intel.instructions.Mov;
import backend.intel.instructions.Pop;
import backend.intel.instructions.Push;
import backend.intel.instructions.Ret;
import backend.intel.instructions.address.IntelAddress;
import backend.intel.instructions.address.IntelImmediate;
import backend.intel.instructions.arithmetic.Add;
import backend.intel.instructions.arithmetic.IntelArithmeticLogic;
import backend.intel.instructions.arithmetic.Sal;
import backend.intel.instructions.directives.CFIEndProc;
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
import frontend.node.stat.SwitchNode;
import frontend.node.stat.WhileNode;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import utils.backend.LabelGenerator;
import utils.backend.register.Register;
import utils.backend.register.arm.ARMConcreteRegister;
import utils.backend.register.intel.IntelConcreteRegister;
import utils.backend.register.intel.IntelConcreteRegisterAllocator;

import static backend.arm.instructions.STR.StrMode.STR;
import static backend.arm.instructions.STR.StrMode.STRB;
import static backend.arm.instructions.addressing.AddressingMode2.AddrMode2.OFFSET;
import static backend.arm.instructions.arithmeticLogic.ARMArithmeticLogic.armUnopAsm;
import static utils.Utils.FALSE;
import static utils.Utils.SystemCallInstruction.EXIT;
import static utils.Utils.TRUE;
import static utils.backend.register.arm.ARMConcreteRegister.SP;
import static utils.backend.register.arm.ARMConcreteRegister.r0;
import static utils.backend.register.arm.ARMConcreteRegister.r4;
import static utils.backend.register.intel.IntelConcreteRegister.*;

public class IntelInstructionGenerator extends InstructionGenerator<IntelInstruction> {

  /* label generators for data section and branches */
  private final LabelGenerator<Label> branchLabelGenerator;
  private final LabelGenerator<Label> dataLabelGenerator;

  /* intel regsiter allocator */
  private final IntelConcreteRegisterAllocator intelRegAllocator;

  /* the data section in the assembly */
  private final Map<Label, String> dataSection;

  public IntelInstructionGenerator() {
    branchLabelGenerator = new LabelGenerator<>(".L", Label.class);
    dataLabelGenerator = new LabelGenerator<>(".LC", Label.class);
    intelRegAllocator = new IntelConcreteRegisterAllocator();
    dataSection = new LinkedHashMap<>();
  }

  @Override
  public Void visitArrayElemNode(ArrayElemNode node) {
    /* get the address of this array and store it in an available register */
    IntelConcreteRegister addrReg = intelRegAllocator.allocate();
    int offset = currSymbolTable.getSize()
        - currSymbolTable.getStackOffset(node.getName(), node.getSymbol())
        + stackOffset;
    instructions.add(new Mov(new IntelAddress(rbp, offset), addrReg));

    IntelConcreteRegister indexReg;
    for (int i = 0; i < node.getDepth(); i++) {
      /* load the index at depth `i` to the next available register */
      ExprNode index = node.getIndex().get(i);
      if (!(index instanceof IntegerNode)) {
        visit(index);
        indexReg = intelRegAllocator.curr();
        if (isLhs) {
          instructions.add(new Mov(new IntelAddress(indexReg), indexReg));
        }
      } else {
        indexReg = intelRegAllocator.allocate();
        instructions.add(new Mov(new IntelAddress(((IntegerNode) index).getVal()), indexReg));
      }

      int elemSize = i < node.getDepth() - 1 ? 2 : node.getType().getSize() / 2;
      instructions.add(new Sal(new IntelImmediate(elemSize), indexReg));
      instructions.add(new Add(addrReg, indexReg));

      /* free indexReg to make it available for the indexing of the next depth */
      intelRegAllocator.free();
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
      int indexOffset = i * node.getContentSize();
      instructions.add(new Mov(intelRegAllocator.curr(), new IntelAddress(addrReg)));
      instructions.add(new Add(new IntelImmediate(indexOffset), addrReg));
      intelRegAllocator.free();
    }

    intelRegAllocator.free();

    return null;
  }

  @Override
  public Void visitBinopNode(BinopNode node) {
    ExprNode expr1 = node.getExpr1();
    ExprNode expr2 = node.getExpr2();
    Register e1reg, e2reg;

    /* potential optimise here */
    if (expr1.getWeight() >= expr2.getWeight()) {
      visit(expr1);
      visit(expr2);
      e2reg = intelRegAllocator.curr();
      e1reg = intelRegAllocator.last();
    } else {
      visit(expr2);
      visit(expr1);
      e2reg = intelRegAllocator.last();
      e1reg = intelRegAllocator.curr();
    }

    Binop operator = node.getOperator();

    instructions.addAll(IntelArithmeticLogic.intelBinopAsm
         .binopAssemble(e1reg, e2reg, null, operator)
         .stream().map(i -> (IntelInstruction) i).collect(Collectors.toList()));

    if (operator.equals(Binop.DIV)) {
      instructions.add(new Mov(rax.withSize(IntelInstructionSize.L), e1reg));
    } else if (operator.equals(Binop.MOD)) {
      instructions.add(new Mov(rdx.withSize(IntelInstructionSize.L), e1reg));
    }

    if (expr1.getWeight() < expr2.getWeight()) {
      instructions.add(new Mov(e2reg, e1reg));
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
    return null;
  }

  @Override
  public Void visitIdentNode(IdentNode node) {
    int identTypeSize = node.getType().getSize();

    /* put pointer that point to ident's value in stack to next available register */
    int offset = currSymbolTable.getSize()
        - currSymbolTable.getStackOffset(node.getName(), node.getSymbol())
        + stackOffset;

    /* TODO: add Intel move type */

    /* if is lhs, then only put address in register */
    if (isLhs) {
      instructions.add(new Mov(new IntelAddress(offset), intelRegAllocator.allocate()));
    } else {
      /* otherwise, put value in register */
      instructions.add(new Mov(new IntelAddress(rbp, offset), intelRegAllocator.allocate()));
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
    return null;
  }

  @Override
  public Void visitPairNode(PairNode node) {
    return null;
  }

  @Override
  public Void visitStringNode(StringNode node) {
    /* Add msg into the data list */
    String str = node.getString();
    Label msgLabel = dataLabelGenerator.getLabel().asIntelLabel();
    dataSection.put(msgLabel, str);

    /* Add the instructions */
    IntelConcreteRegister reg = intelRegAllocator.allocate();

    instructions.add(new Lea(reg, new IntelAddress(msgLabel)));
    return null;
  }

  @Override
  public Void visitUnopNode(UnopNode node) {
    visit(node.getExpr());

    Register reg = intelRegAllocator.curr();
    Unop operator = node.getOperator();

    List<Instruction> insList = armUnopAsm
        .unopAssemble(reg, reg, operator, null);
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

    IntelConcreteRegister reg = intelRegAllocator.last();
    /* TODO: add intel mov type here */
    instructions.add(new Mov(reg, new IntelAddress(intelRegAllocator.curr())));
    intelRegAllocator.free();
    intelRegAllocator.free();
    return null;
  }

  @Override
  public Void visitDeclareNode(DeclareNode node) {
    visit(node.getRhs());
    int identTypeSize = node.getRhs().getType().getSize();
    /* TODO: add intel move type here */

    int offset = currSymbolTable.getSize() -
        node.getScope().lookup(node.getIdentifier()).getStackOffset();

    instructions.add(new Mov(intelRegAllocator.curr(), new IntelAddress(rbx, offset)));
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
    return null;
  }

  @Override
  public Void visitPrintlnNode(PrintlnNode node) {
    return null;
  }

  @Override
  public Void visitPrintNode(PrintNode node) {
    return null;
  }

  @Override
  public Void visitReadNode(ReadNode node) {
    return null;
  }

  @Override
  public Void visitReturnNode(ReturnNode node) {
    return null;
  }

  @Override
  public Void visitScopeNode(ScopeNode node) {
    return null;
  }

  @Override
  public Void visitSkipNode(SkipNode node) {
    return null;
  }

  @Override
  public Void visitWhileNode(WhileNode node) {
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
    /* 3 PUSH rsp and MOV rsp to rbp */
    instructions.add(new Push(Collections.singletonList(rbp)));
    instructions.add(new Mov(rsp, rbp));

    /* 4 main body */
    visit(node.getBody());

    /* 5 set return value and return */
    instructions.add(new Mov(new IntelAddress(0), rax.withSize(IntelInstructionSize.L)));
    instructions.add(new Pop(rbp));
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
}
