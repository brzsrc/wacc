package backend.arm;

import static backend.arm.instructions.LDR.LdrMode.*;
import static backend.arm.instructions.STR.StrMode.*;
import static backend.arm.instructions.addressing.AddressingMode2.AddrMode2.*;
import static backend.arm.instructions.arithmeticLogic.ARMArithmeticLogic.armUnopAsm;
import static backend.arm.instructions.addressing.ARMImmediate.BitNum.CONST8;
import static backend.arm.instructions.addressing.Operand2.Operand2Operator.*;
import static frontend.node.expr.UnopNode.Unop.MINUS;
import static utils.Utils.RoutineInstruction;
import static utils.Utils.RoutineInstruction.*;
import static utils.Utils.SystemCallInstruction.*;
import static utils.Utils.*;
import static backend.arm.instructions.ARMInstructionRoutines.routineFunctionMap;
import static utils.backend.Cond.*;
import static utils.backend.register.arm.ARMConcreteRegister.*;

import backend.Instruction;
import backend.InstructionGenerator;
import backend.arm.instructions.*;
import backend.arm.instructions.LDR.LdrMode;
import backend.arm.instructions.STR.StrMode;
import backend.arm.instructions.addressing.*;
import backend.arm.instructions.arithmeticLogic.*;
import backend.arm.instructions.Pop;
import backend.arm.instructions.Push;
import backend.common.address.Address;
import frontend.node.*;
import frontend.node.expr.*;
import frontend.node.expr.BinopNode.Binop;
import frontend.node.expr.UnopNode.Unop;
import frontend.node.stat.*;
import frontend.node.stat.JumpNode.JumpType;
import frontend.node.stat.SwitchNode.CaseStat;
import frontend.type.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import utils.backend.LabelGenerator;
import utils.backend.register.arm.ARMConcreteRegister;
import utils.backend.register.arm.ARMConcreteRegisterAllocator;
import utils.backend.register.Register;

public class ARMInstructionGenerator extends InstructionGenerator<ARMInstruction> {

  /* maximum of bytes that can be added/subtracted from the stack pointer */
  public static int MAX_STACK_STEP = (1 << 10);
  /* const used in visitBinop, for checking multiply overflow */
  public static int ASR_SHIFT_CONST = 31;

  /* the ARM concrete register allocator */
  private final ARMConcreteRegisterAllocator armRegAllocator;
  /* the .data section of the assembly code */
  protected final Map<Label, String> dataSegmentMessages;
  /* a list of instructions for storing different helper functions
   * would be appended to the end of instructions list while printing */
  private final List<ARMInstruction> ARMRoutines;
  /* record which helpers already exist, we don't want repeated helper functions */
  private final Set<RoutineInstruction> alreadyExist;

  /* call getLabel() on branchLabelGenerator to get label in the format of "L0, L1, L2, ..." */
  protected final LabelGenerator<Label> branchLabelGenerator;
  /* call getLabel() on msgLabelGenerator to get label in the format of "msg_0, msg_1, msg_2, ..."*/
  protected final LabelGenerator<Label> msgLabelGenerator;

  /* used for mapping type with its print routine function */
  private final Map<Type, RoutineInstruction> typeRoutineMap = Map.of(
    INT_BASIC_TYPE,    PRINT_INT,
    CHAR_BASIC_TYPE,   PRINT_CHAR,
    BOOL_BASIC_TYPE,   PRINT_BOOL,
    STRING_BASIC_TYPE, PRINT_STRING,
    CHAR_ARRAY_TYPE,   PRINT_STRING
  );

  public ARMInstructionGenerator() {
    super();
    armRegAllocator = new ARMConcreteRegisterAllocator();
    ARMRoutines = new ArrayList<>();
    alreadyExist = new HashSet<>();
    branchLabelGenerator = new LabelGenerator<Label>(BRANCH_HEADER, Label.class);
    msgLabelGenerator = new LabelGenerator<>(MSG_HEADER, Label.class);
    dataSegmentMessages = new LinkedHashMap<>();
  }

  @Override
  public Void visitStructElemNode(StructElemNode node) {
    /* get the address of this struct and store it in an available register */
    Register addrReg = armRegAllocator.allocate();
    int offset = currSymbolTable.getSize()
        - currSymbolTable.getStackOffset(node.getName(), node.getSymbol())
        + stackOffset;
    instructions.add(new Add(addrReg, SP, new Operand2(offset)));

    instructions.add(new LDR(addrReg, new AddressingMode2(OFFSET, addrReg)));

    /* depth 0 is always reached */
    instructions.add(new Add(addrReg, addrReg, new Operand2(node.getOffset(0))));

    for (int i = 1; i < node.getDepth(); i++) {
      instructions.add(new LDR(addrReg, new AddressingMode2(OFFSET, addrReg)));
      instructions.add(new Add(addrReg, addrReg, new Operand2(node.getOffset(i))));
    }

    /* if is not lhs, load the struct elem content to `reg` */
    if (!isLhs) {
      instructions.add(new LDR(addrReg, new AddressingMode2(OFFSET, addrReg), LDR));
    }

    return null;
  }

  @Override
  public Void visitStructNode(StructNode node) {
    /* if the struct is not initialised (null) */
    if (!node.isInitialised()) {
      ARMConcreteRegister reg = armRegAllocator.allocate();
      instructions.add(new LDR(reg, new ImmedAddress(0)));
      return null;
    }

    /* load R0 with the number of bytes needed and malloc  */
    instructions.add(new LDR(r0, new ImmedAddress(node.getSize())));
    instructions.add(new BL(MALLOC.toString()));

    /* then MOV the result pointer of the struct to the next available register */
    Register addrReg = armRegAllocator.allocate();
    instructions.add(new Mov(addrReg, new Operand2(r0)));

    /* visit the content */
    for (int i = 0; i < node.getElemCount(); i++) {
      visit(node.getElem(i));
      instructions.add(new STR(armRegAllocator.curr(), new AddressingMode2(OFFSET, addrReg, node.getElemOffset(i)), STR));
      armRegAllocator.free();
    }

    return null;
  }

  /* nothing to do for the StructDeclareNode in the backend */
  @Override
  public Void visitStructDeclareNode(StructDeclareNode node) {
    return null;
  }

  @Override
  public Void visitArrayElemNode(ArrayElemNode node) {
    /* get the address of this array and store it in an available register */
    Register addrReg = armRegAllocator.allocate();
    int offset = currSymbolTable.getSize()
        - currSymbolTable.getStackOffset(node.getName(), node.getSymbol())
        + stackOffset;
    instructions.add(new Add(addrReg, SP, new Operand2(offset)));

    checkAndAddRoutine(CHECK_ARRAY_BOUND, msgLabelGenerator, dataSegmentMessages);
    checkAndAddRoutine(THROW_RUNTIME_ERROR, msgLabelGenerator, dataSegmentMessages);

    Register indexReg;
    for (int i = 0; i < node.getDepth(); i++) {
      /* load the index at depth `i` to the next available register */
      ExprNode index = node.getIndex().get(i);
      if (!(index instanceof IntegerNode)) {
        visit(index);
        indexReg = armRegAllocator.curr();
        if (isLhs) {
          instructions.add(new LDR(indexReg, new AddressingMode2(OFFSET, indexReg)));
        }
      } else {
        indexReg = armRegAllocator.allocate();
        instructions
            .add(new LDR(indexReg, new ImmedAddress(((IntegerNode) index).getVal())));
      }

      /* check array bound */
      instructions.add(new LDR(addrReg, new AddressingMode2(OFFSET, addrReg)));
      instructions.add(new Mov(r0, new Operand2(indexReg)));
      instructions.add(new Mov(r1, new Operand2(addrReg)));
      instructions.add(new BL(CHECK_ARRAY_BOUND.toString()));

      instructions.add(new Add(addrReg, addrReg, new Operand2(ARM_POINTER_SIZE)));

      int elemSize = i < node.getDepth() - 1 ? 2 : node.getType().getSize() / 2;
      instructions.add(new Add(addrReg, addrReg, new Operand2(indexReg, LSL, elemSize)));

      /* free indexReg to make it available for the indexing of the next depth */
      armRegAllocator.free();
    }

    /* if is not lhs, load the array content to `reg` */
    if (!isLhs) {
      instructions.add(new LDR(addrReg, new AddressingMode2(OFFSET, addrReg),
          node.getType().getSize() > 1 ? LdrMode.LDR : LDRSB));
    }
    return null;
  }

  @Override
  public Void visitArrayNode(ArrayNode node) {
    /* get the total number of bytes needed to allocate enough space for the array */
    int size = node.getType() == null ? 0 : node.getContentSize() * node.getLength();
    /* add 4 bytes to `size` to include the size of the array as the first byte */
    size += ARM_POINTER_SIZE;

    /* load R0 with the number of bytes needed and malloc  */
    instructions.add(new LDR(r0, new ImmedAddress(size)));
    instructions.add(new BL(MALLOC.toString()));

    /* then MOV the result pointer of the array to the next available register */
    Register addrReg = armRegAllocator.allocate();

    instructions.add(new Mov(addrReg, new Operand2(r0)));

    /* STR mode used to indicate whether to store a byte or a word */
    StrMode mode = node.getContentSize() > 1 ? STR : STRB;

    for (int i = 0; i < node.getLength(); i++) {
      visit(node.getElem(i));
      int STRIndex = i * node.getContentSize() + WORD_SIZE;
      instructions.add(
          new STR(armRegAllocator.curr(), new AddressingMode2(OFFSET, addrReg, STRIndex), mode));
      armRegAllocator.free();
    }

    Register sizeReg = armRegAllocator.allocate();
    /* STR the size of the array in the first byte */
    instructions.add(new LDR(sizeReg, new ImmedAddress(node.getLength())));
    instructions.add(new STR(sizeReg, new AddressingMode2(OFFSET, addrReg)));

    armRegAllocator.free();

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

    List<Instruction> insList = ARMArithmeticLogic.binopInstruction
        .get(operator)
        .binopAssemble(e1reg, e2reg, op2, operator);
    instructions.addAll(insList.stream().map(i -> (ARMInstruction) i).collect(Collectors.toList()));
    if (operator == Binop.DIV || operator == Binop.MOD) {
      checkAndAddRoutine(CHECK_DIVIDE_BY_ZERO, msgLabelGenerator, dataSegmentMessages);
    }

    if (operator == Binop.PLUS || operator == Binop.MINUS) {
      instructions.add(new BL(VS, THROW_OVERFLOW_ERROR.toString()));
      checkAndAddRoutine(THROW_OVERFLOW_ERROR, msgLabelGenerator, dataSegmentMessages);
    }

    if (operator == Binop.MUL) {
      instructions.add(new Cmp(e2reg, new Operand2(e1reg, ASR, ASR_SHIFT_CONST)));
      instructions.add(new BL(NE, THROW_OVERFLOW_ERROR.toString()));
      checkAndAddRoutine(THROW_OVERFLOW_ERROR, msgLabelGenerator, dataSegmentMessages);
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
    int val = node.getVal() ? TRUE : FALSE;
    Operand2 operand2 = new Operand2(val);
    instructions.add(new Mov(reg, operand2));
    return null;
  }

  @Override
  public Void visitCharNode(CharNode node) {
    ARMConcreteRegister reg = armRegAllocator.allocate();
    ARMImmediate immed = new ARMImmediate(node.getAsciiValue(), CONST8, true);
    instructions.add(new Mov(reg, new Operand2(immed)));
    return null;
  }

  @Override
  public Void visitIntegerNode(IntegerNode node) {
    ARMConcreteRegister reg = armRegAllocator.allocate();
    instructions.add(new LDR(reg, new ImmedAddress(node.getVal())));
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
      visit(expr);
      Register reg = armRegAllocator.curr();
      int size = expr.getType().getSize();
      StrMode mode = size > 1 ? STR : STRB;
      instructions.add(new STR(reg, new AddressingMode2(PREINDEX, SP, -size), mode));
      armRegAllocator.free();

      paramSize += size;
      stackOffset += size;
    }
    stackOffset = 0;

    /* 2 call function with B instruction */
    String overloadName = node.getFunction().getOverloadName();
    String actualFuncName = (overloadName != null) ? overloadName : node.getFunction().getFunctionName();
    instructions.add(new BL(FUNC_HEADER + actualFuncName));

    /* 3 add back stack pointer */
    if (paramSize > 0) {
      instructions.add(new Add(SP, SP, new Operand2(paramSize)));
    }

    /* 4 get result, put in a general register */
    instructions.add(new Mov(armRegAllocator.allocate(), new Operand2(r0)));

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
      mode = LDR;
    } else {
      mode = LDRSB;
    }

    /* if is lhs, then only put address in register */
    if (isLhs) {
      instructions.add(new Add(
          armRegAllocator.allocate(),
          SP, new Operand2(offset)));
    } else {
      /* otherwise, put value in register */
      instructions.add(new LDR(
          armRegAllocator.allocate(),
          new AddressingMode2(OFFSET, SP, offset), mode));
    }
    return null;
  }

  @Override
  public Void visitPairElemNode(PairElemNode node) {
    /* 1 get pointer to the pair from stack
     *   store into next available register
     *   reg is expected register where visit will put value in */

    /* e.g. read fst a, (fst a) is used as lhs but (a) is used as rhs */
    Register reg = armRegAllocator.next();
    boolean isLhsOutside = isLhs;
    isLhs = false;
    visit(node.getPair());
    isLhs = isLhsOutside;

    /* 2 move pair pointer to r0, prepare for null pointer check  */
    instructions.add(new Mov(r0, new Operand2(reg)));

    /* 3 BL null pointer check */
    instructions.add(new BL(CHECK_NULL_POINTER.toString()));

    checkAndAddRoutine(CHECK_NULL_POINTER, msgLabelGenerator, dataSegmentMessages);

    /* 4 get pointer to child
     *   store in the same register, save register space
     *   no need to check whether child has initialised, as it is in lhs */
    AddressingMode2 addrMode;

    if (node.isFirst()) {
      addrMode = new AddressingMode2(OFFSET, reg);
    } else {
      addrMode = new AddressingMode2(OFFSET, reg, ARM_POINTER_SIZE);
    }

    if (isLhs) {
      instructions.add(new LDR(reg, addrMode));
    } else {
      instructions.add(new LDR(reg, addrMode));
      instructions.add(new LDR(reg, new AddressingMode2(OFFSET, reg)));
    }

    return null;
  }

  @Override
  public Void visitPairNode(PairNode node) {
    /* null is also a pairNode
     *  if one of child is null, the other has to be null */
    if (node.getFst() == null || node.getSnd() == null) {
      instructions.add(new LDR(armRegAllocator.allocate(), new ImmedAddress(0)));
      return null;
    }

    /* 1 malloc pair */
    /* 1.1 move size of a pair in r0
     *    pair in heap is 2 pointers, so 8 byte */
    instructions.add(new LDR(r0, new ImmedAddress(2 * ARM_POINTER_SIZE)));

    /* 1.2 BL malloc and get pointer in general use register*/
    instructions.add(new BL(MALLOC.toString()));
    Register pairPointer = armRegAllocator.allocate();

    instructions.add(new Mov(pairPointer, new Operand2(r0)));

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
    instructions.add(new LDR(r0, new ImmedAddress(child.getType().getSize())));

    /* 3 BL malloc, assign child value and get pointer in heap area pairPointer[0] or [1] */
    instructions.add(new BL(MALLOC.toString()));

    StrMode mode = child.getType().getSize() > 1 ? STR : STRB;
    instructions.add(new STR(fstVal, new AddressingMode2(OFFSET, r0), mode));
    instructions.add(new STR(r0, new AddressingMode2(OFFSET, pairPointer, offset)));

    /* free register used for storing child's value */
    armRegAllocator.free();
  }

  @Override
  public Void visitStringNode(StringNode node) {
    /* Add msg into the data list */
    String str = node.getString();
    Label msgLabel = msgLabelGenerator.getLabel().asArmLabel();
    dataSegmentMessages.put(msgLabel, str);

    /* Add the instructions */
    ARMConcreteRegister reg = armRegAllocator.allocate();

    Address strLabel = new ImmedAddress(msgLabel);
    instructions.add(new LDR(reg, strLabel));

    return null;
  }

  @Override
  public Void visitUnopNode(UnopNode node) {
    visit(node.getExpr());
    Register reg = armRegAllocator.curr();
    Unop operator = node.getOperator();

    List<Instruction> insList = armUnopAsm
        .unopAssemble(reg, reg, operator, null);
    instructions.addAll(insList.stream().map(i -> (ARMInstruction) i).collect(Collectors.toList()));

    if (operator == MINUS) {
      instructions.add(new BL(VS, THROW_OVERFLOW_ERROR.toString()));
      checkAndAddRoutine(THROW_OVERFLOW_ERROR, msgLabelGenerator, dataSegmentMessages);
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
    StrMode mode = node.getRhs().getType().getSize() > 1 ? STR : STRB;

    instructions.add(new STR(reg,
        new AddressingMode2(OFFSET, armRegAllocator.curr()), mode));
    armRegAllocator.free();
    armRegAllocator.free();
    return null;
  }

  @Override
  public Void visitDeclareNode(DeclareNode node) {
    visit(node.getRhs());
    int identTypeSize = node.getRhs().getType().getSize();
    StrMode strMode = identTypeSize == 1 ? STRB : STR;

    int offset = currSymbolTable.getSize() -
        node.getScope().lookup(node.getIdentifier()).getStackOffset();

    instructions.add(new STR(armRegAllocator.curr(),
        new AddressingMode2(OFFSET, SP, offset), strMode));
    armRegAllocator.free();
    return null;
  }

  @Override
  public Void visitExitNode(ExitNode node) {
    /* If regs were allocated correctly for every statement
     * then the argument value of exit would be put into r4 */
    visit(node.getValue());
    /* Mov the argument value from r4 to r0 */
    instructions.add(new Mov(r0, new Operand2(r4)));
    /* Call the exit function */
    instructions.add(new BL(EXIT.toString()));

    return null;
  }

  @Override
  public Void visitFreeNode(FreeNode node) {
    visit(node.getExpr());
    instructions.add(new Mov(r0, new Operand2(armRegAllocator.curr())));
    armRegAllocator.free();

    Type type = node.getExpr().getType();
    /* free struct would be the same as free array */
    RoutineInstruction routine = type.equalToType(PAIR_TYPE) ? FREE_PAIR : FREE_ARRAY;

    instructions.add(new BL(routine.toString()));
    checkAndAddRoutine(routine, msgLabelGenerator, dataSegmentMessages);

    return null;
  }

  @Override
  public Void visitIfNode(IfNode node) {
    boolean isIfElse = node.getElseBody() != null;

    Label ifLabel = branchLabelGenerator.getLabel().asArmLabel();
    Label exitLabel = branchLabelGenerator.getLabel().asArmLabel();

    /* 1 condition check, branch */
    visit(node.getCond());
    Register cond = armRegAllocator.curr();
    instructions.add(new Cmp(cond, new Operand2(1)));
    armRegAllocator.free();

    /* 2 elseBody translate, if there is one */
    if (isIfElse) {
      instructions.add(new B(EQ, ifLabel.getName()));
      visit(node.getElseBody());
      instructions.add(new B(NULL, exitLabel.getName()));
    } else {
      instructions.add(new B(NE, exitLabel.getName()));
    }

    /* 3 ifBody translate */
    instructions.add(ifLabel);
    visit(node.getIfBody());

    /* 4 end of if statement */
    instructions.add(exitLabel);

    return null;
  }

  @Override
  public Void visitPrintlnNode(PrintlnNode node) {
    /* print content same as printNode */
    visitPrintNode(new PrintNode(node.getExpr()));

    instructions.add(new BL(PRINT_LN.toString()));
    checkAndAddRoutine(PRINT_LN, msgLabelGenerator, dataSegmentMessages);

    return null;
  }

  @Override
  public Void visitPrintNode(PrintNode node) {
    visit(node.getExpr());
    instructions.add(new Mov(r0, new Operand2(armRegAllocator.curr())));

    Type type = node.getExpr().getType();
    RoutineInstruction routine = typeRoutineMap.getOrDefault(type, PRINT_REFERENCE);

    instructions.add(new BL(routine.toString()));
    checkAndAddRoutine(routine, msgLabelGenerator, dataSegmentMessages);

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
    RoutineInstruction routine = (type.equalToType(INT_BASIC_TYPE)) ? READ_INT : READ_CHAR;
    instructions.add(new Mov(r0, new Operand2(armRegAllocator.curr())));
    instructions.add(new BL(routine.toString()));

    checkAndAddRoutine(routine, msgLabelGenerator, dataSegmentMessages);
    armRegAllocator.free();

    return null;
  }

  @Override
  public Void visitReturnNode(ReturnNode node) {
    visit(node.getExpr());
    instructions.add(new Mov(r0, new Operand2(armRegAllocator.curr())));
    armRegAllocator.free();
    if (funcStackSize != 0) {
      instructions.add(new Add(SP, SP,
          new Operand2(funcStackSize)));
    }
    instructions.add(new Pop(Collections.singletonList(PC)));

    return null;
  }

  @Override
  public Void visitScopeNode(ScopeNode node) {
    List<StatNode> list = node.getBody();

    /* 1 leave space for variables in stack */
    int stackSize = node.getStackSize();
    decStack(stackSize);

    /* accumulate function stack size, in case this scope is a function scope and contain return */
    funcStackSize += stackSize;
    breakSectionStackSize += stackSize;
    continueSectionStackSize += stackSize;

    /* 2 visit statements
     *   set currentSymbolTable here, eliminate all other set symbol table in other statNode */
    currSymbolTable = node.getScope();
    for (StatNode elem : list) {
      visit(elem);
    }
    currSymbolTable = currSymbolTable.getParentSymbolTable();

    /* decrease function stack size, as from this point stack is freed by the scope, not by return */
    funcStackSize -= stackSize;
    breakSectionStackSize -= stackSize;
    continueSectionStackSize -= stackSize;

    /* 3 restore stack */
    incStack(stackSize);

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
    Label testLabel = branchLabelGenerator.getLabel().asArmLabel();
    if (!node.isDoWhile()) {
      instructions.add(new B(NULL, testLabel.getName()));
    }

    /* 2 get a label, mark the start of the loop */
    Label startLabel = branchLabelGenerator.getLabel().asArmLabel();
    Label nextLabel = branchLabelGenerator.getLabel().asArmLabel();

    /* restore the last jump-to label after visiting the while-loop body */
    Label lastBreakJumpToLabel = currBreakJumpToLabel == null ? null : currBreakJumpToLabel.asArmLabel();
    Label lastContinueJumpToLabel = currContinueJumpToLabel == null ? null : currContinueJumpToLabel.asArmLabel();
    currBreakJumpToLabel = nextLabel;
    currContinueJumpToLabel = testLabel;

    instructions.add(startLabel);

    /* record how much stack parent loop used */
    int prevBreakLoopSize = breakSectionStackSize;
    int prevContinueLoopSize = continueSectionStackSize;
    breakSectionStackSize = 0;
    continueSectionStackSize = 0;

    /* 3 loop body */
    visit(node.getBody());
    
    /* restore parent loop stack size */
    breakSectionStackSize = prevBreakLoopSize;
    continueSectionStackSize = prevContinueLoopSize;

    currBreakJumpToLabel = lastBreakJumpToLabel;
    currContinueJumpToLabel = lastContinueJumpToLabel;

    /* 4 start of condition test */
    instructions.add(testLabel);
    /* translate cond expr */
    visit(node.getCond());
    instructions.add(new Cmp(armRegAllocator.curr(), new Operand2(TRUE)));

    /* 5 conditional branch jump to the start of loop */
    instructions.add(new B(EQ, startLabel.getName()));

    instructions.add(nextLabel);

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
    String overloadName = node.getOverloadName();
    String actualFuncName = (overloadName != null) ? overloadName : node.getFunctionName();
    instructions.add(new Label(FUNC_HEADER + actualFuncName));
    instructions.add(new Push(Collections.singletonList(LR)));

    /* 2 decrease stack, leave space for variable in function body
     *   DOES NOT include parameters' stack area */
    if (funcStackSize != 0) {
      instructions.add(new Sub(SP, SP,
          new Operand2(funcStackSize)));
    }

    /* 3 visit function,
     *   RETURN are responsible for adding stack back
     */
    visit(node.getFunctionBody());

    /* function always add pop and ltorg at the end of function body */
    instructions.add(new Pop(Collections.singletonList(PC)));
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
    Label mainLabel = new Label(MAIN_BODY_NAME);
    instructions.add(mainLabel);
    /* 3 PUSH {lr} */
    instructions.add(new Push(Collections.singletonList(LR)));

    /* 4 main body */
    visit(node.getBody());

    /* 5 set exit value */
    instructions.add(new LDR(r0, new ImmedAddress(0)));

    /* 6 POP {PC} .ltorg */
    instructions.add(new Pop(Collections.singletonList(PC)));
    instructions.add(new LTORG());
    return null;
  }

  /* below are helper functions used in this class */
  private void checkAndAddRoutine(RoutineInstruction routine, LabelGenerator<Label> labelGenerator,
      Map<Label, String> dataSegment) {
    Map<RoutineInstruction, RoutineInstruction> linkedRoutines = Map.of(
        THROW_RUNTIME_ERROR, PRINT_STRING,
        FREE_ARRAY, THROW_RUNTIME_ERROR,
        FREE_PAIR, THROW_RUNTIME_ERROR,
        CHECK_NULL_POINTER, THROW_RUNTIME_ERROR,
        CHECK_DIVIDE_BY_ZERO, THROW_RUNTIME_ERROR,
        THROW_OVERFLOW_ERROR, THROW_RUNTIME_ERROR
    );

    if (!alreadyExist.contains(routine)) {
      alreadyExist.add(routine);
      ARMRoutines.addAll(routineFunctionMap.get(routine)
          .routineFunctionAssemble(routine, labelGenerator, dataSegment));

      if (linkedRoutines.containsKey(routine)) {
        RoutineInstruction r = linkedRoutines.get(routine);
        checkAndAddRoutine(r, labelGenerator, dataSegment);
        alreadyExist.add(r);
      }
    }
  }

  /* below are getter and setter of this class */

  public List<ARMInstruction> getInstructions() {
    instructions.addAll(ARMRoutines);
    return instructions;
  }

  public Map<Label, String> getDataSegmentMessages() {
    return dataSegmentMessages;
  }

  @Override
  public Void visitForNode(ForNode node) {
    /* 1 translate the initiator of the for-loop */
    currSymbolTable = node.getIncrement().getScope();
    int stackSize = currSymbolTable.getSize();
    decStack(stackSize);

    visit(node.getInit());

    /* 2 create labels and translate for for-loop body */
    Label bodyLabel = branchLabelGenerator.getLabel().asArmLabel();
    Label condLabel = branchLabelGenerator.getLabel().asArmLabel();
    Label nextLabel = branchLabelGenerator.getLabel().asArmLabel();
    Label incrementLabel = branchLabelGenerator.getLabel().asArmLabel();

    /* restore the last jump-to label after visiting the for-loop body */
    Label lastBreakJumpToLabel = currBreakJumpToLabel == null ? null : currBreakJumpToLabel.asArmLabel();
    Label lastContinueJumpToLabel = currContinueJumpToLabel == null ? null : currContinueJumpToLabel.asArmLabel();
    currBreakJumpToLabel = nextLabel;
    currContinueJumpToLabel = incrementLabel;

    instructions.add(new B(NULL, condLabel.getName()));

    instructions.add(bodyLabel);

    /* record now much stack loop body have occupied */
    int prevBreakLoopSize = breakSectionStackSize;
    int prevContinueLoopSize = continueSectionStackSize;
    breakSectionStackSize = 0;
    continueSectionStackSize = 0;

    visit(node.getBody());

    /* restore parent loop stack size */
    breakSectionStackSize = prevBreakLoopSize;
    continueSectionStackSize = prevContinueLoopSize;
    
    /* here we also need to append the for-loop increment at the end */
    instructions.add(incrementLabel);
    visit(node.getIncrement());

    currBreakJumpToLabel = lastBreakJumpToLabel;
    currContinueJumpToLabel = lastContinueJumpToLabel;

    /* 3 add label for condition checking */
    instructions.add(condLabel);

    currSymbolTable = node.getIncrement().getScope();
    visit(node.getCond());

    currSymbolTable = currSymbolTable.getParentSymbolTable();
    instructions.add(new Cmp(armRegAllocator.curr(), new Operand2(TRUE)));
    armRegAllocator.free();
    /* 4 conditional branch jump to the start of loop */
    instructions.add(new B(EQ, bodyLabel.getName()));
    
    /* 5 add the label for the following instructions after for-loop */
    instructions.add(nextLabel);
    
    /* loop varient's stack clearing should be after next label, 
       so that BREAK can add back stack used by varients */
    incStack(stackSize);

    return null;
  }

  @Override
  public Void visitJumpNode(JumpNode node) {
    if (node.getJumpType().equals(JumpType.BREAK)) {
      /* this snippet is to deal with for-loop stack difference */
      incStack(breakSectionStackSize);
      instructions.add(new B(NULL, currBreakJumpToLabel.getName()));
    } else if (node.getJumpType().equals(JumpType.CONTINUE)) {
      incStack(continueSectionStackSize);
      instructions.add(new B(NULL, currContinueJumpToLabel.getName()));
    }

    return null;
  }

  @Override
  public Void visitSwitchNode(SwitchNode node) {
    visit(node.getExpr());

    List<Label> cLabels = new ArrayList<>();
    
    for (CaseStat c : node.getCases()) {
      visit(c.getExpr());
      Label cLabel = branchLabelGenerator.getLabel().asArmLabel();
      cLabels.add(cLabel);
      instructions.add(new Cmp(armRegAllocator.last(), new Operand2(armRegAllocator.curr())));
      instructions.add(new B(EQ, cLabel.getName()));
      armRegAllocator.free();
    }

    Label defaultLabel = branchLabelGenerator.getLabel().asArmLabel();
    Label afterLabel = branchLabelGenerator.getLabel().asArmLabel();

    /* restore the jump-to label after visiting the switch cases and default */
    Label lastBreakJumpToLabel = currBreakJumpToLabel == null ? null : currBreakJumpToLabel.asArmLabel();
    currBreakJumpToLabel = afterLabel;

    instructions.add(new B(NULL, defaultLabel.getName()));

    for (int i = 0; i < cLabels.size(); i++) {
      instructions.add(cLabels.get(i));
      /* record now much stack switch body have occupied */
      int prevLoopSize = breakSectionStackSize;
      breakSectionStackSize = 0;
      visit(node.getCases().get(i).getBody());
      /* restore parent switch stack size */
      breakSectionStackSize = prevLoopSize;
    }

    int prevLoopSize = breakSectionStackSize;
    breakSectionStackSize = 0;

    instructions.add(defaultLabel);
    visit(node.getDefault());
    
    breakSectionStackSize = prevLoopSize;
    currBreakJumpToLabel = lastBreakJumpToLabel;

    instructions.add(afterLabel);

    return null;
  }

  private void incStack(int stackSize) {
    while (stackSize > 0) {
      int realStackSize = stackSize / MAX_STACK_STEP >= 1 ? MAX_STACK_STEP : stackSize;
      instructions.add(new Add(SP, SP,
              new Operand2(realStackSize)));
      stackSize = stackSize - realStackSize;
    }
  }

  private void decStack(int stackSize) {
    while (stackSize > 0) {
      int realStackSize = stackSize / MAX_STACK_STEP >= 1 ? MAX_STACK_STEP : stackSize;
      instructions.add(new Sub(SP, SP,
              new Operand2(realStackSize)));
      stackSize = stackSize - realStackSize;
    }
  }

}
