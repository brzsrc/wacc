package backend;

import backend.instructions.*;
import backend.instructions.addressing.ImmediateAddressing;
import backend.instructions.addressing.addressingMode2.AddressingMode2;
import backend.instructions.addressing.addressingMode2.AddressingMode2.AddrMode2;
import backend.instructions.arithmeticLogic.Add;
import backend.instructions.arithmeticLogic.ArithmeticLogic;
import backend.instructions.memory.ARMStack;
import backend.instructions.operand.Immediate;
import backend.instructions.operand.Operand2;
import backend.instructions.operand.Operand2.Operand2Operator;
import frontend.node.*;
import frontend.node.expr.*;
import frontend.node.expr.BinopNode.Binop;
import frontend.node.stat.*;
import utils.NodeVisitor;
import utils.backend.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static backend.instructions.operand.Immediate.BitNum;
import static utils.Utils.*;

public class ARMInstructionGenerator implements NodeVisitor<Void> {

  /*
   * the pseudo-register allocator used to generate an infinite supply of
   * registers
   */
  private static PseudoRegisterAllocator pseudoRegAllocator;
  /* the ARM conrete register allocator */
  private static ARMConcreteRegisterAllocator armRegAllocator;
  /* a list of instructions represent the entire program */
  private static List<Instruction> instructions;
  /* the mapping between register and ident */
  private static ARMStack stack;
  /* the mapping between stack address and ident */
  private static Map<String, Integer> identStackMap;
  /* call getLabel on labelGenerator to get label in format LabelN */
  private LabelGenerator labelGenerator;

  public ARMInstructionGenerator() {
    pseudoRegAllocator = new PseudoRegisterAllocator();
    armRegAllocator = new ARMConcreteRegisterAllocator();
    instructions = new ArrayList<>();
    stack = new ARMStack();
    identStackMap = new HashMap<>();
    labelGenerator = new LabelGenerator();
  }

  @Override
  public Void visitArrayElemNode(ArrayElemNode node) {
    /* get the address of this array and store it in an available register */
    Register addrReg = armRegAllocator.allocate();
    Operand2 operand2 = new Operand2(new Immediate(identStackMap.get(node.getName()), BitNum.CONST8));
    instructions.add(new Add(addrReg, armRegAllocator.get(ARMRegisterLabel.SP), operand2));

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

      instructions.add(new Add(addrReg, addrReg, new Operand2(new Immediate(4, BitNum.CONST8))));
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
    instructions.add(new BL("malloc"));

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
    Register e1reg = armRegAllocator.last();
    Binop operator = node.getOperator();
    Operand2 op2 = new Operand2(e2reg);

    List<Instruction> insList = ArithmeticLogic.binopInstruction
                                               .get(operator)
                                               .binopAssemble(e1reg, e1reg, op2, operator);
    instructions.addAll(insList);
    
    return null;
  }

  @Override
  public Void visitBoolNode(BoolNode node) {
    ARMConcreteRegister reg = armRegAllocator.allocate();
    Immediate immed = new Immediate(node.getVal() ? TRUE : FALSE, BitNum.SHIFT32);
    Operand2 operand2 = new Operand2(immed);
    // instructions.add(new Mov(reg, operand2));
    return null;
  }

  @Override
  public Void visitCharNode(CharNode node) {
    ARMConcreteRegister reg = armRegAllocator.allocate();
    Immediate immed = new Immediate(node.getAsciiValue(), BitNum.SHIFT32);
    Operand2 operand2 = new Operand2(immed);
    // instructions.add(new Mov(reg, operand2));
    return null;
  }

  @Override
  public Void visitIntegerNode(IntegerNode node) {
    // todo: same as visitCharNode
    return null;
  }

  @Override
  public Void visitFunctionCallNode(FunctionCallNode node) {
    // todo: sx119
    /*
     * 1 compute parameters, all parameter in stack also add into function's
     * identmap
     */
    for (ExprNode expr : node.getParams()) {
      visit(expr);
      // todo: use STR to store in stack, no need to change symbol table
    }

    /* 2 call function with B instruction */

    /* 3 get result, put in register */
    return null;
  }

  @Override
  public Void visitIdentNode(IdentNode node) {
    String identName = node.getName();
    /* if ident appear for the first time, return a new sudo reg */
    // if (identMap.containsKey(identName)) {
    // return identMap.get(identName);
    // }
    /* new ident should be handled in declare node or related function node */
    throw new IllegalArgumentException(
        "new Ident should be handled in visitDeclareNode or visitFuncNode, not in visitIdentNode");
  }

  @Override
  public Void visitPairElemNode(PairElemNode node) {
    /* TODO: xz1919 */
    return null;
  }

  @Override
  public Void visitPairNode(PairNode node) {
    /* TODO: xz1919 */
    return null;
  }

  @Override
  public Void visitStringNode(StringNode node) {
    /* TODO: xx1219 */
    return null;
  }

  @Override
  public Void visitUnopNode(UnopNode node) {
    // TODO: left over
    return null;
  }

  @Override
  public Void visitAssignNode(AssignNode node) {
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
    /* TODO: xx1219 */
    return null;
  }

  @Override
  public Void visitFreeNode(FreeNode node) {
    /* TODO: xz1919 */
    return null;
  }

  @Override
  public Void visitIfNode(IfNode node) {
    Label ifLabel = labelGenerator.getLabel();
    Label elseLabel = labelGenerator.getLabel();
    Label exitLabel = labelGenerator.getLabel();

    /* 1 condition check, branch */
    visit(node.getCond());
    instructions.add(new B(Cond.EQ, ifLabel.toString()));

    /* 2 ifBody translate */
    instructions.add(ifLabel);
    visit(node.getIfBody());
    instructions.add(new B(Cond.NULL, exitLabel.toString()));

    /* 3 elseBody translate */
    instructions.add(elseLabel);
    visit(node.getElseBody());

    /* 4 end of if statement */
    instructions.add(exitLabel);

    return null;
  }

  @Override
  public Void visitPrintlnNode(PrintlnNode node) {
    /* TODO: xx1219 */
    return null;
  }

  @Override
  public Void visitPrintNode(PrintNode node) {
    /* TODO: xx1219 */
    return null;
  }

  @Override
  public Void visitReadNode(ReadNode node) {
    /* TODO: xx1219 */
    return null;
  }

  @Override
  public Void visitReturnNode(ReturnNode node) {
    /* TODO: xz1919 */
    return null;
  }

  @Override
  public Void visitScopeNode(ScopeNode node) {
    // todo: sx119: reserve space for idents in stack
    List<StatNode> list = node.getBody();

    for (StatNode elem : list) {
      visit(elem);
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
    visit(node.getCond());

    /* 5 conditional branch jump to the start of loop */
    new B(Cond.EQ, startLabel.toString());

    return null;
  }

  @Override
  public Void visitFuncNode(FuncNode node) {
    /* TODO: xz1919 */
    return null;
  }

  @Override
  public Void visitProgramNode(ProgramNode node) {
    /*  */
    return null;
  }
}
