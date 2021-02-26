package backend;

import backend.directives.Label;
import backend.instructions.*;
import backend.instructions.addressing.Addressing;
import backend.instructions.addressing.ImmediateAddressing;
import backend.instructions.addressing.addressingMode2.AddressingMode2;
import backend.instructions.addressing.addressingMode2.AddressingMode2.AddrMode2;
import backend.instructions.arithmeticLogic.Add;
import backend.instructions.arithmeticLogic.Sub;
import backend.instructions.arithmeticLogic.ArithmeticLogic;
import backend.instructions.operand.Immediate;
import backend.instructions.operand.Operand2;
import backend.instructions.operand.Operand2.Operand2Operator;
import frontend.node.*;
import frontend.node.expr.*;
import frontend.node.expr.BinopNode.Binop;
import frontend.node.stat.*;
import utils.NodeVisitor;
import utils.backend.*;
import utils.frontend.symbolTable.SymbolTable;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.*;

import static backend.instructions.operand.Immediate.BitNum;
import static utils.Utils.*;

public class ARMInstructionGenerator implements NodeVisitor<Void> {

  /* the ARM conrete register allocator */
  private static ARMConcreteRegisterAllocator armRegAllocator = new ARMConcreteRegisterAllocator();;
  /* lists of instruction, data section messages and text section messages */
  private static List<Instruction> instructions = new ArrayList<>();
  private static List<String> dataSegmentMessages = new ArrayList<>();
  private static List<String> textSegmentMessages = new ArrayList<>();
  /* record the current symbolTable used during instruction generation */
  private SymbolTable currSymbolTable;
  /* call getLabel on labelGenerator to get label in format LabelN */

  /* a list of instructions for storing different helper functions
   * would be appended to the end of instructions list while printing */
  private static List<Instruction> helperFunctions;

  /* call getLabel on labelGenerator to get label in format LabelN */
  private LabelGenerator labelGenerator;

  /* constant fields */
  private Register SP;

  /* special instruction mapping */
  /* TODO: 把这里完善一下 */
  public enum SpecialInstruction { MALLOC, PRINT, PRINT_INT, PRINT_BOOL, PRINTLN, CHECK_ARRAY_BOUND }
  public static final Map<SpecialInstruction, String> specialInstructions = Map.ofEntries(
    new AbstractMap.SimpleEntry<SpecialInstruction, String>(SpecialInstruction.MALLOC, "malloc"),
    new AbstractMap.SimpleEntry<SpecialInstruction, String>(SpecialInstruction.CHECK_ARRAY_BOUND, "p_check_array_bounds")
  );

  public ARMInstructionGenerator() {
    currSymbolTable = null;
    labelGenerator = new LabelGenerator("L");
    /* initialise constant fields */
    SP = armRegAllocator.get(ARMRegisterLabel.SP);
  }

  @Override
  public Void visitArrayElemNode(ArrayElemNode node) {
    /* get the address of this array and store it in an available register */
    Register addrReg = armRegAllocator.allocate();
    Operand2 operand2 = new Operand2(new Immediate(currSymbolTable.getStackOffset(node.getName()), BitNum.CONST8));
    instructions.add(new Add(addrReg, SP, operand2));

    /* TODO: make a helper function out of this */
    for (int i = 0; i < node.getDepth(); i++) {
      /* load the index at depth `i` to the next available register */
      Register indexReg = armRegAllocator.allocate();
      ExprNode index = node.getIndex().get(i);
      if (!(index instanceof IntegerNode)) {
        visit(index);
      } else {
        instructions.add(new LDR(indexReg, new ImmediateAddressing(new Immediate(((IntegerNode) index).getVal(), BitNum.CONST8))));
      }

      /* check array bound */
      instructions.add(new LDR(addrReg, new AddressingMode2(AddrMode2.OFFSET, addrReg)));
      instructions.add(new Mov(armRegAllocator.get(0), new Operand2(indexReg)));
      instructions.add(new Mov(armRegAllocator.get(1), new Operand2(addrReg)));
      instructions.add(new BL("p_check_array_bounds"));

      instructions.add(new Add(addrReg, addrReg, new Operand2(new Immediate(POINTER_SIZE, BitNum.CONST8))));
      instructions.add(new Add(addrReg, addrReg, new Operand2(indexReg, Operand2Operator.LSL, new Immediate(2, BitNum.CONST8))));

      /* free indexReg to make it available for the indexing of the next depth */
      armRegAllocator.free();
    }

    /* now load the array content to `reg` */
    instructions.add(new LDR(addrReg, new AddressingMode2(AddrMode2.OFFSET, addrReg)));

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

    /* then allocate the content of the array to the corresponding address */
    for (int i = 0; i < node.getLength(); i++) {
      Register reg = armRegAllocator.allocate();
      visit(node.getElem(i));
      int STRIndex = i * WORD_SIZE + WORD_SIZE;
      instructions.add(new LDR(reg, new AddressingMode2(AddrMode2.OFFSET, armRegAllocator.curr())));
      instructions.add(new STR(addrReg, new AddressingMode2(AddrMode2.OFFSET, reg, new Immediate(STRIndex, BitNum.CONST8))));
      armRegAllocator.free();
    }

    /* STR the size of the array in the first byte */
    Register sizeReg = armRegAllocator.allocate();
    instructions.add(new LDR(sizeReg, new ImmediateAddressing(new Immediate(size, BitNum.CONST8))));
    instructions.add(new STR(sizeReg, new AddressingMode2(AddrMode2.OFFSET, addrReg)));

    armRegAllocator.free();

    return null;
  }

  @Override
  public Void visitBinopNode(BinopNode node) {
    visit(node.getExpr1());
    visit(node.getExpr2());
    Register e2reg = armRegAllocator.curr();
    // todo: get rid of last() function, replace by calling curr() or next() before visit
    Register e1reg = armRegAllocator.last();
    Binop operator = node.getOperator();
    Operand2 op2 = new Operand2(e2reg);

    List<Instruction> insList = ArithmeticLogic.binopInstruction
                                               .get(operator)
                                               .binopAssemble(e1reg, e1reg, op2, operator);
    instructions.addAll(insList);
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
    Immediate immed = new Immediate(node.getAsciiValue(), BitNum.SHIFT32);
    instructions.add(new LDR(reg, new ImmediateAddressing(immed)));
    return null;
  }

  @Override
  public Void visitIntegerNode(IntegerNode node) {
    // todo: same as visitCharNode
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
    for (ExprNode expr : params) {
      Register reg = armRegAllocator.next();
      visit(expr);
      int size = expr.getType().getSize();
      instructions.add(new STR(reg,new AddressingMode2(AddrMode2.PREINDEX, SP, new Immediate(-size, BitNum.CONST8))));

      paramSize += size;
    }

    /* 2 call function with B instruction */
    instructions.add(new BL("f_" + node.getFunction().getFunctionName()));

    /* 3 add back stack pointer */
    instructions.add(new Add(SP, SP, new Operand2(new Immediate(paramSize, BitNum.SHIFT32))));

    /* 4 get result, put in register */
    instructions.add(new Mov(armRegAllocator.get(ARMRegisterLabel.R0), new Operand2(armRegAllocator.allocate())));
    return null;
  }

  @Override
  public Void visitIdentNode(IdentNode node) {
    String identName = node.getName();

    /* put pointer that point to ident's value in stack to next available register */
    int offset = currSymbolTable.getStackOffset(identName);
    instructions.add(new Add(
            armRegAllocator.allocate(),
            SP,
            new Operand2(new Immediate(offset, BitNum.SHIFT32))));
    return null;
  }

  @Override
  public Void visitPairElemNode(PairElemNode node) {
    /* 1 get pointer to the pair from stack
     *   store into next available register
     *   reg is expected register where visit will put value in */
    Register reg = armRegAllocator.next();
    visit(node.getPair());

    /* 2 move pair pointer to r0, prepare for null pointer check  */
    instructions.add(new Mov(armRegAllocator.get(ARMRegisterLabel.R0), new Operand2(reg)));

    /* 3 BL null pointer check */
    // todo: add check null pointer to collection of predefine functions
    instructions.add(new BL("p_check_null_pointer"));

    /* 4 get pointer to child
    *    store in the same register, save register space
    *    no need to check whether child has initialised, as it is in lhs */
    if (node.isFist()) {
      instructions.add(new LDR(reg, new AddressingMode2(AddrMode2.OFFSET, reg)));
    } else {
      instructions.add(new LDR(reg, new AddressingMode2(AddrMode2.OFFSET, reg, new Immediate(POINTER_SIZE, BitNum.CONST8))));
    }

    return null;
  }

  @Override
  public Void visitPairNode(PairNode node) {

    /* 1 malloc pair */
    /* 1.1 move size of a pair in r0
    *    pair in heap is 2 pointers, so 8 byte */
    instructions.add(new LDR(armRegAllocator.get(ARMRegisterLabel.R0), new ImmediateAddressing(new Immediate(2 * POINTER_SIZE, BitNum.CONST8))));

    /* 1.2 BL malloc and get pointer in general use register*/
    instructions.add(new BL(specialInstructions.get(SpecialInstruction.MALLOC)));
    Register pairPointer = armRegAllocator.allocate();
    instructions.add(new Mov(pairPointer, new Operand2(armRegAllocator.get(ARMRegisterLabel.R0))));

    /* 2 visit both child */
    visitPairChildExpr(node.getFst(), pairPointer, true);

    visitPairChildExpr(node.getSnd(), pairPointer, false);

    return null;
  }

  private void visitPairChildExpr(ExprNode child, Register pairPointer, boolean isFst) {
    /* 1 visit fst expression, get result in general register */
    Register fstVal = armRegAllocator.next();
    visit(child);

    /* 2 move size of fst child in r0 */
    instructions.add(
            new LDR(armRegAllocator.get(ARMRegisterLabel.R0),
                    new ImmediateAddressing(new Immediate(child.getType().getSize(), BitNum.CONST8))));

    /* 3 BL malloc, assign child value and get pointer in heap area pairPointer[0] */
    instructions.add(new BL(specialInstructions.get(SpecialInstruction.MALLOC)));
    instructions.add(new STR(fstVal, new AddressingMode2(AddrMode2.OFFSET, armRegAllocator.get(ARMRegisterLabel.R0))));
    if (isFst) {
      instructions.add(new STR(armRegAllocator.get(ARMRegisterLabel.R0), new AddressingMode2(AddrMode2.OFFSET, pairPointer)));
    } else {
      instructions.add(new STR(armRegAllocator.get(ARMRegisterLabel.R0), new AddressingMode2(AddrMode2.OFFSET, pairPointer, new Immediate(POINTER_SIZE, BitNum.CONST8))));
    }

  }

  @Override
  public Void visitStringNode(StringNode node) {
    /* Add msg into the data list */
    Label msg = labelGenerator.getLabel();
    dataSegmentMessages.add(node.getString());

    /* Add the instructions */
    ARMConcreteRegister reg = armRegAllocator.allocate();
    Addressing strLabel = new ImmediateAddressing(msg.getName());
    instructions.add(new LDR(reg, strLabel));

    return null;
  }

  @Override
  public Void visitUnopNode(UnopNode node) {
    // TODO: left over
    return null;
  }

  @Override
  public Void visitAssignNode(AssignNode node) {
    // todo: use type check, if is identnode, use addressing to put value in stack in one instruction
    currSymbolTable = node.getScope();
    visit(node.getRhs());
    ARMConcreteRegister reg = armRegAllocator.curr();
    visit(node.getLhs());
    instructions.add(new STR(reg,
        new AddressingMode2(AddrMode2.OFFSET, armRegAllocator.curr())));
    armRegAllocator.free();
    armRegAllocator.free();
    return null;
  }

  @Override
  public Void visitDeclareNode(DeclareNode node) {
    // todo: same as visitAssignNode
    currSymbolTable = node.getScope();
    /* the returned value is now in r4 */
    visit(node.getRhs());
    instructions.add(new STR(armRegAllocator.curr(),
        new AddressingMode2(AddrMode2.OFFSET, armRegAllocator.get(ARMRegisterLabel.SP),
            new Immediate(node.getScope().getStackOffset(node.getIdentifier()), BitNum.CONST8))));
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
    currSymbolTable = node.getScope();
    /* TODO: xz1919 */
    return null;
  }

  @Override
  public Void visitIfNode(IfNode node) {
    Label ifLabel = labelGenerator.getLabel();
    Label exitLabel = labelGenerator.getLabel();

    /* 1 condition check, branch */
    currSymbolTable = node.getScope();
    visit(node.getCond());
    instructions.add(new B(Cond.EQ, ifLabel.toString()));

    /* 2 elseBody translate */
    visit(node.getElseBody());
    instructions.add(new B(Cond.NULL, exitLabel.toString()));

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
    instructions.add(new Mov(armRegAllocator.get(0), new Operand2(armRegAllocator.get(4))));
    HelperFunction.addPrint(node.getExpr().getType(), instructions, dataSegmentMessages, helperFunctions, armRegAllocator);
    HelperFunction.addPrintln(instructions, dataSegmentMessages, helperFunctions, armRegAllocator);
    return null;
  }

  @Override
  public Void visitPrintNode(PrintNode node) {
    visit(node.getExpr());
    instructions.add(new Mov(armRegAllocator.get(0), new Operand2(armRegAllocator.get(4))));
    HelperFunction.addPrint(node.getExpr().getType(), instructions, dataSegmentMessages, helperFunctions, armRegAllocator);
    return null;
  }

  @Override
  public Void visitReadNode(ReadNode node) {
    /* TODO: visit the "address" of node.getInputExpr() and Mov r4 int or0 */
    HelperFunction.addRead(node.getInputExpr().getType(), instructions, dataSegmentMessages, helperFunctions, armRegAllocator);
    return null;
  }

  @Override
  public Void visitReturnNode(ReturnNode node) {
    currSymbolTable = node.getScope();
    /* TODO: xz1919 */
    return null;
  }

  @Override
  public Void visitScopeNode(ScopeNode node) {
    List<StatNode> list = node.getBody();

    int stackSize = node.getStackSize();
    /* if necessary, change stack pointer for storing variable */
    if (stackSize != 0) {
      instructions.add(new Sub(SP, SP,
              new Operand2(new Immediate(stackSize, BitNum.SHIFT32))));
    }
    for (StatNode elem : list) {
      visit(elem);
    }

    if (stackSize != 0) {
      instructions.add(new Add(SP, SP,
              new Operand2(new Immediate(stackSize, BitNum.SHIFT32))));
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
    instructions.add(new B(Cond.NULL, testLabel.toString()));

    /* 2 get a label, mark the start of the loop */
    Label startLabel = labelGenerator.getLabel();
    instructions.add(startLabel);

    /* 3 loop body */
    visit(node.getBody());

    /* 4 start of condition test */
    instructions.add(testLabel);
    /* translate cond expr */
    currSymbolTable = node.getScope();
    visit(node.getCond());

    /* 5 conditional branch jump to the start of loop */
    new B(Cond.EQ, startLabel.toString());

    return null;
  }

  @Override
  public Void visitFuncNode(FuncNode node) {
    int stackSize = node.getFunctionBody().getScope().getSize();
    for (IdentNode ident : node.getParamList()) {
      stackSize -= ident.getType().getSize();
    }
    if (stackSize != 0) {
      instructions.add(new Sub(SP, SP,
              new Operand2(new Immediate(stackSize, BitNum.SHIFT32))));
    }
    currSymbolTable = node.getFunctionBody().getScope();
    visit(node.getFunctionBody());

    if (stackSize != 0) {
      instructions.add(new Add(SP, SP,
              new Operand2(new Immediate(stackSize, BitNum.SHIFT32))));
    }
    return null;
  }

  @Override
  public Void visitProgramNode(ProgramNode node) {
    Map<String, FuncNode> funcMap = node.getFunctions();
    for (Entry<String, FuncNode> entry : funcMap.entrySet()) {
      visit(entry.getValue());
    }
    
    Label mainLabel = new Label("main");
    instructions.add(mainLabel);

    visit(node.getBody());

    return null;
  }

  /* below are getter and setter of this class */

  public static List<Instruction> getInstructions() {
    return instructions;
  }

  public static List<String> getDataSegmentMessages() {
    return dataSegmentMessages;
  }

  public static List<String> getTextSegmentMessages() {
    return textSegmentMessages;
  }
  
}
