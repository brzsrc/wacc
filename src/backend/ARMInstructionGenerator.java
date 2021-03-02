package backend;

import backend.instructions.LDR.LdrMode;
import backend.instructions.STR.StrMode;
import backend.instructions.*;
import backend.instructions.addressing.Addressing;
import backend.instructions.addressing.ImmediateAddressing;
import backend.instructions.addressing.LabelAddressing;
import backend.instructions.addressing.addressingMode2.AddressingMode2;
import backend.instructions.addressing.addressingMode2.AddressingMode2.AddrMode2;
import backend.instructions.arithmeticLogic.Add;
import backend.instructions.arithmeticLogic.Sub;
import backend.instructions.memory.Pop;
import backend.instructions.memory.Push;
import backend.instructions.arithmeticLogic.ArithmeticLogic;
import backend.instructions.operand.Immediate;
import backend.instructions.operand.Operand2;
import backend.instructions.operand.Operand2.Operand2Operator;
import frontend.node.*;
import frontend.node.expr.*;
import frontend.node.expr.BinopNode.Binop;
import frontend.node.expr.UnopNode.Unop;
import frontend.node.stat.*;
import frontend.type.Type;

import java.util.LinkedHashMap;
import utils.NodeVisitor;
import utils.backend.*;
import utils.frontend.symbolTable.SymbolTable;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static backend.instructions.operand.Immediate.BitNum;
import static utils.Utils.*;

public class ARMInstructionGenerator implements NodeVisitor<Void> {

  /* the ARM conrete register allocator */
  private static ARMConcreteRegisterAllocator armRegAllocator = new ARMConcreteRegisterAllocator();;
  /* lists of instruction, data section messages and text section messages */
  private static List<Instruction> instructions = new ArrayList<>();
  /* use linkedHashMap to ensure the correct ordering */
  private static Map<Label, String> dataSegmentMessages = new LinkedHashMap<>();
  private static List<String> textSegmentMessages = new ArrayList<>();
  /* maximum of bytes that can be added/subtracted from the stack pointer */
  public static int MAX_STACK_STEP = 1024;

  /* record the current symbolTable used during instruction generation */
  private SymbolTable currSymbolTable;
  /* call getLabel on labelGenerator to get label in format LabelN */

  /* a list of instructions for storing different helper functions
   * would be appended to the end of instructions list while printing */
  private static List<Instruction> helperFunctions = new ArrayList<>();

  /* call getLabel on labelGenerator to get label in format LabelN */
  private LabelGenerator labelGenerator;

  /* mark if we are visiting a lhs or rhs of an expr */
  private boolean isLhs;

  /* offset used when pushing variable in stack in visitFunctionCall 
   * USED FOR evaluating function parameters, not for changing parameters' offset in function body*/
  private int stackOffset;

  /* constant fields */
  private Register SP;

  /* special instruction mapping */
  public enum SpecialInstruction { MALLOC, PRINT, PRINT_INT, PRINT_BOOL, PRINTLN, CHECK_ARRAY_BOUND }
  public static final Map<SpecialInstruction, String> specialInstructions = Map.ofEntries(
    new AbstractMap.SimpleEntry<SpecialInstruction, String>(SpecialInstruction.MALLOC, "malloc"),
    new AbstractMap.SimpleEntry<SpecialInstruction, String>(SpecialInstruction.CHECK_ARRAY_BOUND, "p_check_array_bounds")
  );

  public ARMInstructionGenerator() {
    currSymbolTable = null;
    labelGenerator = new LabelGenerator("L");
    stackOffset = 0;
    isLhs = false;
    /* initialise constant fields */
    SP = armRegAllocator.get(ARMRegisterLabel.SP);
  }

  @Override
  public Void visitArrayElemNode(ArrayElemNode node) {
    /* get the address of this array and store it in an available register */
    Register addrReg = armRegAllocator.allocate();
    int offset = currSymbolTable.getSize() - (currSymbolTable.getStackOffset(node.getName(), node.getSymbol()) + POINTER_SIZE) + stackOffset;
    Operand2 operand2 = new Operand2(new Immediate(offset, BitNum.CONST8));
    instructions.add(new Add(addrReg, SP, operand2));

    HelperFunction.addCheckArrayBound(dataSegmentMessages, helperFunctions, armRegAllocator);
    HelperFunction.addThrowRuntimeError(dataSegmentMessages, helperFunctions, armRegAllocator);

    Register indexReg;
    for (int i = 0; i < node.getDepth(); i++) {
      /* load the index at depth `i` to the next available register */
      ExprNode index = node.getIndex().get(i);
      if (!(index instanceof IntegerNode)) {
        visit(index);
        indexReg = armRegAllocator.curr();
        if (isLhs) {
          instructions.add(new LDR(indexReg, new AddressingMode2(AddrMode2.OFFSET, indexReg)));
        }
      } else {
        indexReg = armRegAllocator.allocate();
        instructions.add(new LDR(indexReg, new ImmediateAddressing(new Immediate(((IntegerNode) index).getVal(), BitNum.CONST8))));
      }
      
      /* check array bound */
      instructions.add(new LDR(addrReg, new AddressingMode2(AddrMode2.OFFSET, addrReg)));
      instructions.add(new Mov(armRegAllocator.get(0), new Operand2(indexReg)));
      instructions.add(new Mov(armRegAllocator.get(1), new Operand2(addrReg)));
      instructions.add(new BL(specialInstructions.get(SpecialInstruction.CHECK_ARRAY_BOUND)));

      instructions.add(new Add(addrReg, addrReg, new Operand2(new Immediate(POINTER_SIZE, BitNum.CONST8))));
      instructions.add(new Add(addrReg, addrReg, new Operand2(indexReg, Operand2Operator.LSL, new Immediate(2, BitNum.CONST8))));

      /* free indexReg to make it available for the indexing of the next depth */
      armRegAllocator.free();
    }

    /* if is not lhs, load the array content to `reg` */
    if (!isLhs) {
      instructions.add(new LDR(addrReg, new AddressingMode2(AddrMode2.OFFSET, addrReg)));
    }
    return null;
  }

  @Override
  public Void visitArrayNode(ArrayNode node) {
    /* get the total number of bytes needed to allocate enought space for the array */
    int size = node.getType() == null ? 0 : node.getContentSize() * node.getLength();
    /* add 4 bytes to `size` to include the size of the array as the first byte */
    size += POINTER_SIZE;

    /* load R0 with the number of bytes needed and malloc  */
    instructions.add(new LDR(armRegAllocator.get(0), new ImmediateAddressing(new Immediate(size, BitNum.CONST8))));
    instructions.add(new BL(specialInstructions.get(SpecialInstruction.MALLOC)));

    /* then MOV the result pointer of the array to the next available register */
    Register addrReg = armRegAllocator.allocate();

    instructions.add(new Mov(addrReg, new Operand2(armRegAllocator.get(0))));

    /* STR mode used to indicate whether to store a byte or a word */
    StrMode mode = node.getContentSize() > 1 ? StrMode.STR : StrMode.STRB;

    for (int i = 0; i < node.getLength(); i++) {
      visit(node.getElem(i));
      int STRIndex = i * node.getContentSize() + WORD_SIZE;
      instructions.add(new STR(armRegAllocator.curr(), new AddressingMode2(AddrMode2.OFFSET, addrReg, new Immediate(STRIndex, BitNum.CONST8)), mode));
      armRegAllocator.free();
    }

    Register sizeReg = armRegAllocator.allocate();
    /* STR the size of the array in the first byte */
    instructions.add(new LDR(sizeReg, new ImmediateAddressing(new Immediate(node.getLength(), BitNum.CONST8))));
    instructions.add(new STR(sizeReg, new AddressingMode2(AddrMode2.OFFSET, addrReg)));

    armRegAllocator.free();

    return null;
  }

  @Override
  public Void visitBinopNode(BinopNode node) {
    ExprNode expr1 = node.getExpr1();
    ExprNode expr2 = node.getExpr2();
    Register e1reg, e2reg;

    /* potential optimise here */
    if(expr1.getWeight() >= expr2.getWeight()) {
      visit(expr1);
      visit(expr2);
      e2reg = armRegAllocator.curr();
      e1reg = armRegAllocator.last();
    } else {
      visit(expr2);
      visit(expr1);
      e2reg = armRegAllocator.last();
      e1reg = armRegAllocator.curr();
    }

    Binop operator = node.getOperator();
    Operand2 op2 = new Operand2(e2reg);

    List<Instruction> insList = ArithmeticLogic.binopInstruction
                                               .get(operator)
                                               .binopAssemble(e1reg, e1reg, op2, operator);
    instructions.addAll(insList);
    if(operator == Binop.DIV || operator == Binop.MOD) {
      HelperFunction.addCheckDivByZero(dataSegmentMessages, helperFunctions, armRegAllocator);
    }

    Binop binop = operator;
    if (binop == Binop.PLUS) {
      instructions.add(new BL(Cond.VS,"p_throw_overflow_error"));
      HelperFunction.addThrowOverflowError(dataSegmentMessages, helperFunctions, armRegAllocator);
    }

    if (binop == Binop.MUL) {
      instructions.add(new Cmp(e1reg, new Operand2(e2reg, Operand2Operator.ASR, new Immediate(31, BitNum.CONST8))));
      instructions.add(new BL(Cond.NE, "p_throw_overflow_error"));
    }

    if (expr1.getWeight() < expr2.getWeight()) {
      instructions.add(new Mov(e2reg, new Operand2(e1reg)));
    }
    armRegAllocator.free();
    
    return null;
  }

  @Override
  public Void visitBoolNode(BoolNode node) {
    ARMConcreteRegister reg = armRegAllocator.allocate();

    Immediate immed = new Immediate(node.getVal() ? TRUE : FALSE, BitNum.SHIFT32);
    Operand2 operand2 = new Operand2(immed);
    instructions.add(new Mov(reg, operand2));
    return null;
  }

  @Override
  public Void visitCharNode(CharNode node) {
    ARMConcreteRegister reg = armRegAllocator.allocate();

    Immediate immed = new Immediate(node.getAsciiValue(), BitNum.SHIFT32, true);
    instructions.add(new Mov(reg, new Operand2(immed)));
    return null;
  }

  @Override
  public Void visitIntegerNode(IntegerNode node) {
    ARMConcreteRegister reg = armRegAllocator.allocate();

    Immediate immed = new Immediate(node.getVal(), BitNum.SHIFT32);
    instructions.add(new LDR(reg, new ImmediateAddressing(immed)));
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

    for (int i = paramNum - 1; i >= 0; i--) {
      ExprNode expr = params.get(i);
      Register reg = armRegAllocator.next();
      visit(expr);
      int size = expr.getType().getSize();
      instructions.add(new STR(reg,new AddressingMode2(AddrMode2.PREINDEX, SP, new Immediate(-size, BitNum.CONST8))));
      armRegAllocator.free();

      paramSize += size;
      stackOffset += size;
    }
    stackOffset = 0;

    /* 2 call function with B instruction */
    instructions.add(new BL("f_" + node.getFunction().getFunctionName()));

    /* 3 add back stack pointer */
    if (paramSize > 0) {
      instructions.add(new Add(SP, SP, new Operand2(new Immediate(paramSize, BitNum.SHIFT32))));
    }

    /* 4 get result, put in register */
    instructions.add(new Mov(armRegAllocator.allocate(), new Operand2(armRegAllocator.get(ARMRegisterLabel.R0))));

    return null;
  }

  @Override
  public Void visitIdentNode(IdentNode node) {

    /* put pointer that point to ident's value in stack to next available register */
    int offset = currSymbolTable.getSize() - currSymbolTable.getStackOffset(node.getName(), node.getSymbol()) - node.getType().getSize() + stackOffset;
    LdrMode mode;
    if (node.getType().getSize() > 1) {
       mode = LdrMode.LDR;
    } else if (node.getType().equalToType(BOOL_BASIC_TYPE)) {
      mode = LdrMode.LDRSB;
    } else {
      mode = LdrMode.LDRB;
    }

    Immediate immed = new Immediate(offset, BitNum.CONST8);

    /* if is lhs, then only put address in register */
    if (isLhs) {
      instructions.add(new Add(
            armRegAllocator.allocate(),
            SP, new Operand2(immed)));
    } else {
      /* otherwise, put value in register */
      instructions.add(new LDR(
              armRegAllocator.allocate(),
              new AddressingMode2(AddrMode2.OFFSET, SP, immed), mode));
    }
    return null;
  }

  @Override
  public Void visitPairElemNode(PairElemNode node) {
    /* 1 get pointer to the pair from stack
     *   store into next available register
     *   reg is expected register where visit will put value in */

    //read fst a
    Register reg = armRegAllocator.next();
    boolean isLhsOutside = isLhs;
    isLhs = false;
    visit(node.getPair());
    isLhs = isLhsOutside;

    /* 2 move pair pointer to r0, prepare for null pointer check  */
    instructions.add(new Mov(armRegAllocator.get(ARMRegisterLabel.R0), new Operand2(reg)));

    /* 3 BL null pointer check */
    instructions.add(new BL("p_check_null_pointer"));
    HelperFunction.addCheckNullPointer(dataSegmentMessages, helperFunctions, armRegAllocator);

    /* 4 get pointer to child
    *    store in the same register, save register space
    *    no need to check whether child has initialised, as it is in lhs */
    AddressingMode2 addrMode;
    Operand2 operand2;
    if (node.isFirst()) {
      addrMode = new AddressingMode2(AddrMode2.OFFSET, reg);
      operand2 = new Operand2(new Immediate(0, BitNum.CONST8));
    } else {
      addrMode = new AddressingMode2(AddrMode2.OFFSET, reg, new Immediate(POINTER_SIZE, BitNum.CONST8));
      operand2 = new Operand2(new Immediate(POINTER_SIZE, BitNum.CONST8));
    }

    if (isLhs) {
      //instructions.add(new Add(reg, reg, operand2));
      instructions.add(new LDR(reg, addrMode));
    } else {
      instructions.add(new LDR(reg, addrMode));
      instructions.add(new LDR(reg, new AddressingMode2(AddrMode2.OFFSET, reg)));
    }

    return null;
  }

  @Override
  public Void visitPairNode(PairNode node) {
    /* null is also a pairNode
    *  if one of child is null, the other has to be null */
    if (node.getFst() == null || node.getSnd() == null) {
      instructions.add(new LDR(armRegAllocator.allocate(), new ImmediateAddressing(new Immediate(0, BitNum.CONST8))));
      return null;
    }

    /* 1 malloc pair */
    /* 1.1 move size of a pair in r0
    *    pair in heap is 2 pointers, so 8 byte */
    instructions.add(new LDR(armRegAllocator.get(ARMRegisterLabel.R0), new ImmediateAddressing(new Immediate(2 * POINTER_SIZE, BitNum.CONST8))));

    /* 1.2 BL malloc and get pointer in general use register*/
    instructions.add(new BL(specialInstructions.get(SpecialInstruction.MALLOC)));
    Register pairPointer = armRegAllocator.allocate();

    instructions.add(new Mov(pairPointer, new Operand2(armRegAllocator.get(ARMRegisterLabel.R0))));

    /* 2 visit both child */
    visitPairChildExpr(node.getFst(), pairPointer, 0);
    //visitPairChildExpr(node.getSnd(), pairPointer, node.getFst().getType().getSize());
    /* pair contains two pointers, each with size 4 */
    visitPairChildExpr(node.getSnd(), pairPointer, WORD_SIZE);

    return null;
  }

  private void visitPairChildExpr(ExprNode child, Register pairPointer, int offset) {
    /* 1 visit fst expression, get result in general register */
    Register fstVal = armRegAllocator.next();
    visit(child);

    /* 2 move size of fst child in r0 */
    instructions.add(
            new LDR(armRegAllocator.get(ARMRegisterLabel.R0),
                    new ImmediateAddressing(new Immediate(child.getType().getSize(), BitNum.CONST8))));

    /* 3 BL malloc, assign child value and get pointer in heap area pairPointer[0] or [1] */
    instructions.add(new BL(specialInstructions.get(SpecialInstruction.MALLOC)));

    StrMode mode = child.getType().getSize() > 1 ? StrMode.STR : StrMode.STRB;
    instructions.add(new STR(fstVal, new AddressingMode2(AddrMode2.OFFSET, armRegAllocator.get(ARMRegisterLabel.R0)), mode));
    instructions.add(new STR(armRegAllocator.get(ARMRegisterLabel.R0), new AddressingMode2(AddrMode2.OFFSET, pairPointer, new Immediate(offset, BitNum.CONST8))));

    /* free register used for storing child's value */
    armRegAllocator.free();

  }

  @Override
  public Void visitStringNode(StringNode node) {
    /* Add msg into the data list */
    Label msg = HelperFunction.addMsg(node.getString(), dataSegmentMessages);

    /* Add the instructions */
    ARMConcreteRegister reg = armRegAllocator.allocate();

    Addressing strLabel = new LabelAddressing(msg);
    instructions.add(new LDR(reg, strLabel));

    return null;
  }

  @Override
  public Void visitUnopNode(UnopNode node) {
    visit(node.getExpr());
    Register reg = armRegAllocator.curr();
    Unop operator = node.getOperator();

    List<Instruction> insList = ArithmeticLogic.unopInstruction
        .get(operator)
        .unopAssemble(reg, reg);
    instructions.addAll(insList);
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

    ARMConcreteRegister reg = armRegAllocator.last();
    StrMode mode = node.getRhs().getType().getSize() > 1 ? StrMode.STR : StrMode.STRB ;

    instructions.add(new STR(reg,
        new AddressingMode2(AddrMode2.OFFSET, armRegAllocator.curr()), mode));
    armRegAllocator.free();
    armRegAllocator.free();
    return null;
  }

  @Override
  public Void visitDeclareNode(DeclareNode node) {
    visit(node.getRhs());
    StrMode strMode = node.getRhs().getType().getSize() == 1 ? StrMode.STRB : StrMode.STR;
    int offset = currSymbolTable.getSize() - 
                  (node.getScope().lookup(node.getIdentifier()).getStackOffset() + 
                  node.getRhs().getType().getSize());

    instructions.add(new STR(armRegAllocator.curr(),
        new AddressingMode2(AddrMode2.OFFSET, armRegAllocator.get(ARMRegisterLabel.SP),
            new Immediate(offset, BitNum.CONST8)), strMode));
    armRegAllocator.free();
    return null;
  }

  @Override
  public Void visitExitNode(ExitNode node) {
    /* If regs were allocated correctly for every statement
     * then the argument value of exit would be put into r4 */
    visit(node.getValue());
    /* Mov the argument value from r4 to r0 */
    instructions.add(new Mov(armRegAllocator.get(0), new Operand2(armRegAllocator.get(4))));
    /* Call the exit function */
    instructions.add(new BL("exit"));

    return null;
  }

  @Override
  public Void visitFreeNode(FreeNode node) {
    // currSymbolTable = node.getScope();
    visit(node.getExpr());
    instructions.add(new Mov(armRegAllocator.get(0), new Operand2(armRegAllocator.curr())));
    armRegAllocator.free();
    Type type = node.getExpr().getType();
    if(type.equalToType(ARRAY_TYPE)) {
      instructions.add(new BL("p_free_array"));
      HelperFunction.addFree(type, dataSegmentMessages, helperFunctions, armRegAllocator);
    } else {
      instructions.add(new BL("p_free_pair"));
      HelperFunction.addFree(type, dataSegmentMessages, helperFunctions, armRegAllocator);
    }
    return null; 
  }

  @Override
  public Void visitIfNode(IfNode node) {
    Label ifLabel = labelGenerator.getLabel();
    Label exitLabel = labelGenerator.getLabel();

    /* 1 condition check, branch */
    visit(node.getCond());
    instructions.add(new B(Cond.EQ, ifLabel.getName()));
    armRegAllocator.free();

    /* 2 elseBody translate */
    visit(node.getElseBody());
    instructions.add(new B(Cond.NULL, exitLabel.getName()));

    /* 3 ifBody translate */
    instructions.add(ifLabel);
    visit(node.getIfBody());

    /* 4 end of if statement */
    instructions.add(exitLabel);

    return null;
  }

  @Override
  public Void visitPrintlnNode(PrintlnNode node) {
    visit(node.getExpr());
    instructions.add(new Mov(armRegAllocator.get(0), new Operand2(armRegAllocator.curr())));
    HelperFunction.addPrint(node.getExpr().getType(), instructions, dataSegmentMessages, helperFunctions, armRegAllocator);
    HelperFunction.addPrintln(instructions, dataSegmentMessages, helperFunctions, armRegAllocator);
    armRegAllocator.free();
    return null;
  }

  @Override
  public Void visitPrintNode(PrintNode node) {
    visit(node.getExpr());
    instructions.add(new Mov(armRegAllocator.get(0), new Operand2(armRegAllocator.curr())));
    HelperFunction.addPrint(node.getExpr().getType(), instructions, dataSegmentMessages, helperFunctions, armRegAllocator);
    armRegAllocator.free();
    return null;
  }

  @Override
  public Void visitReadNode(ReadNode node) {
    isLhs = true;
    visit(node.getInputExpr());
    isLhs = false;
    instructions.add(new Mov(armRegAllocator.get(0), new Operand2(armRegAllocator.curr())));
    HelperFunction.addRead(node.getInputExpr().getType(), instructions, dataSegmentMessages, helperFunctions, armRegAllocator);
    armRegAllocator.free();
    return null;
  }

  @Override
  public Void visitReturnNode(ReturnNode node) {
    visit(node.getExpr());
    instructions.add(new Mov(armRegAllocator.get(0), new Operand2(armRegAllocator.curr())));
    armRegAllocator.free();
    return null;
  }

  @Override
  public Void visitScopeNode(ScopeNode node) {
    List<StatNode> list = node.getBody();

    int stackSize = node.getStackSize();

    int temp = stackSize;
    while (temp > 0) {
      int realStackSize = temp / MAX_STACK_STEP >= 1 ? MAX_STACK_STEP : temp;
      instructions.add(new Sub(SP, SP,
              new Operand2(new Immediate(realStackSize, BitNum.SHIFT32))));
      temp = temp - realStackSize;
    }

    currSymbolTable = node.getScope();
    for (StatNode elem : list) {
      visit(elem);
    }
    currSymbolTable = currSymbolTable.getParentSymbolTable();

    temp = stackSize;
    while (temp > 0) {
      int realStackSize = temp / MAX_STACK_STEP >= 1 ? MAX_STACK_STEP : temp;
      instructions.add(new Add(SP, SP,
              new Operand2(new Immediate(realStackSize, BitNum.SHIFT32))));
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
    Label testLabel = labelGenerator.getLabel();
    instructions.add(new B(Cond.NULL, testLabel.getName()));

    /* 2 get a label, mark the start of the loop */
    Label startLabel = labelGenerator.getLabel();
    instructions.add(startLabel);

    /* 3 loop body */
    visit(node.getBody());

    /* 4 start of condition test */
    instructions.add(testLabel);
    /* translate cond expr */
    visit(node.getCond());
    instructions.add(new Cmp(armRegAllocator.curr(), new Operand2(new Immediate(TRUE, BitNum.CONST8))));

    /* 5 conditional branch jump to the start of loop */
    instructions.add(new B(Cond.EQ, startLabel.getName()));

    armRegAllocator.free();

    return null;
  }

  @Override
  public Void visitFuncNode(FuncNode node) {
    /* cannot call get stack size on function body, as that will return 0 */
    int stackSize = node.getFunctionBody().getScope().getSize();
    stackSize -= node.paramListStackSize();
    instructions.add(new Label("f_" + node.getFunctionName()));
    instructions.add(new Push(Collections.singletonList(armRegAllocator.get(14))));
    if (stackSize != 0) {
      instructions.add(new Sub(SP, SP,
              new Operand2(new Immediate(stackSize, BitNum.SHIFT32))));
    }
    // currSymbolTable = node.getFunctionBody().getScope();
    visit(node.getFunctionBody());
    if (stackSize != 0) {
      instructions.add(new Add(SP, SP,
              new Operand2(new Immediate(stackSize, BitNum.SHIFT32))));
    }
    instructions.add(new Pop(Collections.singletonList(armRegAllocator.get(ARMRegisterLabel.PC))));
    instructions.add(new Pop(Collections.singletonList(armRegAllocator.get(ARMRegisterLabel.PC))));
    instructions.add(new LTORG());
    return null;
  }

  @Override
  public Void visitProgramNode(ProgramNode node) {
    // currSymbolTable = node.getBody().getScope();

    for (FuncNode func : node.getFunctions().values()) {
      visitFuncNode(func);
    }
    
    Label mainLabel = new Label("main");
    instructions.add(mainLabel);
    instructions.add(new Push(Collections.singletonList(armRegAllocator.get(ARMRegisterLabel.LR))));
    visit(node.getBody());
    instructions.add(new LDR(armRegAllocator.get(0), new ImmediateAddressing(new Immediate(0, BitNum.CONST8))));
    instructions.add(new Pop(Collections.singletonList(armRegAllocator.get(ARMRegisterLabel.PC))));

    return null;
  }

  /* below are getter and setter of this class */

  public static List<Instruction> getInstructions() {
    instructions.addAll(helperFunctions);
    return instructions;
  }

  public static Map<Label, String> getDataSegmentMessages() {
    return dataSegmentMessages;
  }

  public static List<String> getTextSegmentMessages() {
    return textSegmentMessages;
  }
  
}
