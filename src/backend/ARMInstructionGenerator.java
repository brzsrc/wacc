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
import java.util.LinkedList;

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

  /* a list of instructions for storing different helper functions
   * would be appended to the end of instructions list while printing */
  private Map<RoutineInstruction, List<Instruction>> helperFunctions = new LinkedHashMap<>();
  /* constant fields */
  private static Register SP = new ARMConcreteRegister(ARMRegisterLabel.SP);

  /* maximum of bytes that can be added/subtracted from the stack pointer */
  public static int MAX_STACK_STEP = 1024;

  /* the ARM conrete register allocator */
  private final ARMConcreteRegisterAllocator armRegAllocator;
  /* the code section of the assembly code */
  private final List<Instruction> instructions;
  /* the .data section of the assembly code */
  private final Map<Label, String> dataSegmentMessages;
  /* the .text section of the assembly code */
  private final List<String> textSegmentMessages;
  /* record the current symbolTable used during instruction generation */
  private SymbolTable currSymbolTable;
  /* call getLabel() on branchLabelGenerator to get label in the format of "L0, L1, L2, ..." */
  private final LabelGenerator branchLabelGenerator;
  /* call getLabel() on msgLabelGenerator to get label in the format of "msg_0, msg_1, msg_2, ..."*/
  private final LabelGenerator msgLabelGenerator;
  /* mark if we are visiting a lhs or rhs of an expr */
  private boolean isLhs;

  /* offset used when pushing variable in stack in visitFunctionCall 
   * USED FOR evaluating function parameters, not for changing parameters' offset in function body*/
  private int stackOffset;

  /* used by visitFunc and visitReturn, set how many byte this function used on stack */
  private int funcStackSize;
  
  public static final Map<SystemCallInstruction, String> specialInstructions = Map.ofEntries(
    new AbstractMap.SimpleEntry<SystemCallInstruction, String>(SystemCallInstruction.MALLOC, "malloc")
  );

  public ARMInstructionGenerator() {
    armRegAllocator = new ARMConcreteRegisterAllocator();
    instructions = new ArrayList<>();
    dataSegmentMessages = new LinkedHashMap<>();
    textSegmentMessages = new LinkedList<>();
    currSymbolTable = null;
    branchLabelGenerator = new LabelGenerator("L");
    msgLabelGenerator = new LabelGenerator("msg_");
    stackOffset = 0;
    isLhs = false;
  }

  @Override
  public Void visitArrayElemNode(ArrayElemNode node) {
    /* get the address of this array and store it in an available register */
    Register addrReg = armRegAllocator.allocate();
    int offset = currSymbolTable.getSize() 
                 - currSymbolTable.getStackOffset(node.getName(), node.getSymbol()) 
                 + stackOffset;
    Operand2 operand2 = new Operand2(new Immediate(offset, BitNum.CONST8));
    instructions.add(new Add(addrReg, SP, operand2));

    checkAndAddRoutine(RoutineInstruction.CHECK_ARRAY_BOUND, ARMInstructionRoutines.addCheckArrayBound(msgLabelGenerator, dataSegmentMessages));
    checkAndAddRoutine(RoutineInstruction.THROW_RUNTIME_ERROR, ARMInstructionRoutines.addThrowRuntimeError(msgLabelGenerator, dataSegmentMessages));

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
      instructions.add(new BL(specialInstructions.get(RoutineInstruction.CHECK_ARRAY_BOUND)));

      instructions.add(new Add(addrReg, addrReg, new Operand2(new Immediate(POINTER_SIZE, BitNum.CONST8))));
      
      Map<Integer, Integer> arrayElemLSLMapping = Map.of(4, 2, 2, 1, 1, 0);
      int elemSize = arrayElemLSLMapping.get(node.getType().getSize());
      instructions.add(new Add(addrReg, addrReg, new Operand2(indexReg, Operand2Operator.LSL, new Immediate(elemSize, BitNum.CONST8))));
      
      /* free indexReg to make it available for the indexing of the next depth */
      armRegAllocator.free();
    }

    /* if is not lhs, load the array content to `reg` */
    if (!isLhs) {
      instructions.add(new LDR(addrReg, new AddressingMode2(AddrMode2.OFFSET, addrReg), node.getType().getSize() > 1 ? LdrMode.LDR : LdrMode.LDRSB));
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
    instructions.add(new BL(specialInstructions.get(SystemCallInstruction.MALLOC)));

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
      checkAndAddRoutine(RoutineInstruction.CHECK_DIVIDE_BY_ZERO, ARMInstructionRoutines.addCheckDivByZero(msgLabelGenerator, dataSegmentMessages));
    }

    Binop binop = operator;
    if (binop == Binop.PLUS || operator == Binop.MINUS) {
      instructions.add(new BL(Cond.VS,RoutineInstruction.THROW_OVERFLOW_ERROR.toString()));
      checkAndAddRoutine(RoutineInstruction.THROW_OVERFLOW_ERROR, ARMInstructionRoutines.addThrowOverflowError(msgLabelGenerator, dataSegmentMessages));
    }

    if (binop == Binop.MUL) {
      instructions.add(new Cmp(e2reg, new Operand2(e1reg, Operand2Operator.ASR, new Immediate(31, BitNum.CONST8))));
      instructions.add(new BL(Cond.NE, RoutineInstruction.THROW_OVERFLOW_ERROR.toString()));
      Label overflowMsgLabel = msgLabelGenerator.getLabel();
      dataSegmentMessages.put(overflowMsgLabel, "\"OverflowError: the result is too small/large to store in a 4-byte signed-integer.\\n\\0\"");
      Label printMultipleLabel = msgLabelGenerator.getLabel();
      dataSegmentMessages.put(printMultipleLabel, "\"%.*s\\0\"");
      checkAndAddRoutine(RoutineInstruction.THROW_OVERFLOW_ERROR, ARMInstructionRoutines.addThrowOverflowError(msgLabelGenerator, dataSegmentMessages));
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
      StrMode mode = size > 1 ? StrMode.STR : StrMode.STRB;
      instructions.add(new STR(reg,new AddressingMode2(AddrMode2.PREINDEX, SP, new Immediate(-size, BitNum.CONST8)), mode));
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

    int identTypeSize = node.getType().getSize();
    /* put pointer that point to ident's value in stack to next available register */
    int offset = currSymbolTable.getSize() 
                 - currSymbolTable.getStackOffset(node.getName(), node.getSymbol()) 
                 + stackOffset;
    LdrMode mode;
    if (identTypeSize > 1) {
       mode = LdrMode.LDR;
    } else {
      mode = LdrMode.LDRSB;
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


    checkAndAddRoutine(RoutineInstruction.CHECK_NULL_POINTER, ARMInstructionRoutines.addCheckNullPointer(msgLabelGenerator, dataSegmentMessages));

    /* 4 get pointer to child
     *   store in the same register, save register space
     *   no need to check whether child has initialised, as it is in lhs */
    AddressingMode2 addrMode;
    Operand2 operand2;
    if (node.isFirst()) {
      addrMode = new AddressingMode2(AddrMode2.OFFSET, reg);
    } else {
      addrMode = new AddressingMode2(AddrMode2.OFFSET, reg, new Immediate(POINTER_SIZE, BitNum.CONST8));
    }

    if (isLhs) {
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
    instructions.add(new BL(specialInstructions.get(SystemCallInstruction.MALLOC)));
    Register pairPointer = armRegAllocator.allocate();

    instructions.add(new Mov(pairPointer, new Operand2(armRegAllocator.get(ARMRegisterLabel.R0))));

    /* 2 visit both child */
    visitPairChildExpr(node.getFst(), pairPointer, 0);
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
    instructions.add(new BL(specialInstructions.get(SystemCallInstruction.MALLOC)));

    StrMode mode = child.getType().getSize() > 1 ? StrMode.STR : StrMode.STRB;
    instructions.add(new STR(fstVal, new AddressingMode2(AddrMode2.OFFSET, armRegAllocator.get(ARMRegisterLabel.R0)), mode));
    instructions.add(new STR(armRegAllocator.get(ARMRegisterLabel.R0), new AddressingMode2(AddrMode2.OFFSET, pairPointer, new Immediate(offset, BitNum.CONST8))));

    /* free register used for storing child's value */
    armRegAllocator.free();

  }

  @Override
  public Void visitStringNode(StringNode node) {
    /* Add msg into the data list */
    String str = node.getString();
    Label msgLabel = new Label(str);
    dataSegmentMessages.put(msgLabel, str);

    /* Add the instructions */
    ARMConcreteRegister reg = armRegAllocator.allocate();

    Addressing strLabel = new LabelAddressing(msgLabel);
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

    if (operator == Unop.MINUS) {
      instructions.add(new BL(Cond.VS,"p_throw_overflow_error"));
      Label msgLabel = msgLabelGenerator.getLabel();
      dataSegmentMessages.put(msgLabel, "\"OverflowError: the result is too small/large to store in a 4-byte signed-integer.\\n\\0\"");
      Label printMultipleLabel = msgLabelGenerator.getLabel();
      dataSegmentMessages.put(printMultipleLabel, "\"%.*s\\0\"");
      checkAndAddRoutine(RoutineInstruction.THROW_OVERFLOW_ERROR, ARMInstructionRoutines.addThrowOverflowError(msgLabelGenerator, dataSegmentMessages));
    }

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
    int identTypeSize = node.getRhs().getType().getSize();
    StrMode strMode = identTypeSize == 1 ? StrMode.STRB : StrMode.STR;

    int offset = currSymbolTable.getSize() - 
                 node.getScope().lookup(node.getIdentifier()).getStackOffset();

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
    visit(node.getExpr());
    instructions.add(new Mov(armRegAllocator.get(0), new Operand2(armRegAllocator.curr())));
    armRegAllocator.free();

    Type type = node.getExpr().getType();
    RoutineInstruction routine = type.equalToType(ARRAY_TYPE) ? RoutineInstruction.FREE_ARRAY : RoutineInstruction.FREE_PAIR;

    instructions.add(new BL(routine.toString()));
    checkAndAddRoutine(routine, ARMInstructionRoutines.addFree(routine, msgLabelGenerator, dataSegmentMessages));

    return null;
  }

  @Override
  public Void visitIfNode(IfNode node) {
    Label ifLabel = branchLabelGenerator.getLabel();
    Label exitLabel = branchLabelGenerator.getLabel();

    /* 1 condition check, branch */
    visit(node.getCond());
    Register cond = armRegAllocator.curr();
    instructions.add(new Cmp(cond, new Operand2(new Immediate(1, BitNum.CONST8))));
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

    Type type = node.getExpr().getType();
    RoutineInstruction routine = printTypeRoutineMapping.get(type);

    instructions.add(new BL(routine.toString()));
    checkAndAddRoutine(routine, ARMInstructionRoutines.addPrint(routine, msgLabelGenerator, dataSegmentMessages));
    instructions.add(new BL(routine.toString()));
    checkAndAddRoutine(routine, ARMInstructionRoutines.addPrintln(msgLabelGenerator, dataSegmentMessages));

    armRegAllocator.free();
    return null;
  }

  @Override
  public Void visitPrintNode(PrintNode node) {
    visit(node.getExpr());
    instructions.add(new Mov(armRegAllocator.get(0), new Operand2(armRegAllocator.curr())));

    Type type = node.getExpr().getType();
    RoutineInstruction routine = printTypeRoutineMapping.get(type);

    instructions.add(new BL(routine.toString()));
    checkAndAddRoutine(routine, ARMInstructionRoutines.addPrint(routine, msgLabelGenerator, dataSegmentMessages));

    armRegAllocator.free();
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

    /* choose between read_int and read_char */
    RoutineInstruction routine = (type.equalToType(INT_BASIC_TYPE)) ? RoutineInstruction.READ_INT : RoutineInstruction.READ_CHAR;
    instructions.add(new Mov(armRegAllocator.get(0), new Operand2(armRegAllocator.curr())));
    instructions.add(new BL(routine.toString()));

    checkAndAddRoutine(routine, ARMInstructionRoutines.addRead(routine, msgLabelGenerator, dataSegmentMessages));
    armRegAllocator.free();

    return null;
  }

  @Override
  public Void visitReturnNode(ReturnNode node) {
    visit(node.getExpr());
    instructions.add(new Mov(armRegAllocator.get(0), new Operand2(armRegAllocator.curr())));
    armRegAllocator.free();
    if (funcStackSize != 0) {
      instructions.add(new Add(SP, SP,
              new Operand2(new Immediate(funcStackSize, BitNum.SHIFT32))));
    }
    instructions.add(new Pop(Collections.singletonList(armRegAllocator.get(ARMRegisterLabel.PC))));

    return null;
  }

  @Override
  public Void visitScopeNode(ScopeNode node) {
    List<StatNode> list = node.getBody();

    /* 1 leave space for variables in stack */
    int stackSize = node.getStackSize();
    int temp = stackSize;
    while (temp > 0) {
      int realStackSize = temp / MAX_STACK_STEP >= 1 ? MAX_STACK_STEP : temp;
      instructions.add(new Sub(SP, SP,
              new Operand2(new Immediate(realStackSize, BitNum.SHIFT32))));
      temp = temp - realStackSize;
    }

    /* 2 visit statements
     *   set currentSymbolTable here, eliminate all other set symbol table in other statNode */
    currSymbolTable = node.getScope();
    for (StatNode elem : list) {
      visit(elem);
    }
    currSymbolTable = currSymbolTable.getParentSymbolTable();

    /* 3 restore stack */
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
    Label testLabel = branchLabelGenerator.getLabel();
    instructions.add(new B(Cond.NULL, testLabel.getName()));

    /* 2 get a label, mark the start of the loop */
    Label startLabel = branchLabelGenerator.getLabel();
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
    /* cannot call get stack size on function body, as that will return 0 
     * public field used here, so that on visit return statement, return can add stack back */
    funcStackSize = node.getFunctionBody().getScope().getSize();
    funcStackSize -= node.paramListStackSize();

    /* 1 add function label, 
     *   PUSH {lr}
     */
    instructions.add(new Label("f_" + node.getFunctionName()));
    instructions.add(new Push(Collections.singletonList(armRegAllocator.get(14))));

    /* 2 decrease stack, leave space for variable in function body
     *   DOES NOT include parameters' stack area */
    if (funcStackSize != 0) {
      instructions.add(new Sub(SP, SP,
              new Operand2(new Immediate(funcStackSize, BitNum.SHIFT32))));
    }

    /* 3 visit function, 
     *   RETURN are responsible for adding stack back 
     */
    visit(node.getFunctionBody());

    /* function always add pop and ltorg at the end of function body */
    instructions.add(new Pop(Collections.singletonList(armRegAllocator.get(ARMRegisterLabel.PC))));
    instructions.add(new LTORG());
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
    /* 3 PUSH {lr} */
    instructions.add(new Push(Collections.singletonList(armRegAllocator.get(ARMRegisterLabel.LR))));

    /* 4 main body */
    visit(node.getBody());

    /* 5 set exit value */
    instructions.add(new LDR(armRegAllocator.get(0), new ImmediateAddressing(new Immediate(0, BitNum.CONST8))));
    
    /* 6 POP {PC} .ltorg */
    instructions.add(new Pop(Collections.singletonList(armRegAllocator.get(ARMRegisterLabel.PC))));
    instructions.add(new LTORG());
    return null;
  }

  /* below are helper functions used in this class */
  private void checkAndAddRoutine(RoutineInstruction routine, List<Instruction> list) {
    if (!helperFunctions.containsKey(routine)) {
      helperFunctions.put(routine, list);
    }
  }


  /* below are getter and setter of this class */

  public List<Instruction> getInstructions() {
    helperFunctions.values().forEach(instructions::addAll);
    return instructions;
  }

  public Map<Label, String> getDataSegmentMessages() {
    return dataSegmentMessages;
  }

  public List<String> getTextSegmentMessages() {
    return textSegmentMessages;
  }
  
}
