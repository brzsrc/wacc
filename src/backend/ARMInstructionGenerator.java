package backend;

import backend.instructions.*;
import backend.instructions.arithmeticLogic.Add;
import backend.instructions.operand.Immediate;
import backend.instructions.operand.Operand2;
import frontend.node.*;
import frontend.node.expr.*;
import frontend.node.stat.*;
import utils.NodeVisitor;
import utils.backend.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static backend.instructions.operand.Immediate.BitNum;
import static utils.Utils.*;

public class ARMInstructionGenerator implements NodeVisitor<Register> {

  /* the pseudo-register allocator used to generate an infinite supply of registers */
  private static PseudoRegisterAllocator pseudoRegAllocator;
  /* the ARM conrete register allocator */
  private static ARMConcreteRegisterAllocator armRegAllocator;
  /* a list of instructions represent the entire program */
  private static List<Instruction> instructions;
  /* the mapping between register and ident */
  private static Map<String, Register> identRegMap;
  /* the mapping between stack address and ident */
  private static Map<String, Integer> identStackMap;
  /* call getLabel on labelGenerator to get label in format LabelN */
  private LabelGenerator labelGenerator;

  public ARMInstructionGenerator() {
    pseudoRegAllocator = new PseudoRegisterAllocator();
    armRegAllocator = new ARMConcreteRegisterAllocator();
    instructions = new ArrayList<>();
    identRegMap = new HashMap<>();
    identStackMap = new HashMap<>();
    labelGenerator = new LabelGenerator();
  }

  @Override
  public Register visitArrayElemNode(ArrayElemNode node) {
    List<Instruction> ins = new ArrayList<>();

    /* get the address of this array and store it in an available register */
    Register reg = armRegAllocator.allocate();
    Operand2 operand2 = new Operand2(new Immediate(identStackMap.get(node.getName()), BitNum.CONST8));
    ins.add(new Add(reg, armRegAllocator.get(ARMRegisterLabel.SP), operand2));

    /* load the index to an available register */
    // ins.add(new );

    return null;
  }

  @Override
  public Register visitArrayNode(ArrayNode node) {
    /* TODO: xz1919 */
    /* 1 generate size of array and put into r0 */
    int size;
    if (node.getType() == null) {
      size = 0;
    } else {
      size = node.getElem(0).getType().getSize() * node.getLength();
    }
    size += POINTER_SIZE;

    /* has to use absolute register, not virtual register */
    // todo: need to check at register allocation that r0, r4 is not in use, store if occupied
    // instructions.add(
    //         new Mov(
    //                 new ARMConcreteRegister(ARMRegisterLabel.R0),
    //                 new Operand2(new Immediate(size, BitNum.SHIFT32))));

    /* 2 call malloc, get result from r4 */
    // todo: does the malloc require more register than r0, r4 ?
    instructions.add(new BL("malloc"));

    return new ARMConcreteRegister(ARMRegisterLabel.R4);
  }

  @Override
  public Register visitBinopNode(BinopNode node) {
    /* TODO: left over */
    Register reg1 = visit(node.getExpr1());
    Register reg2 = visit(node.getExpr2());

    /* generate corrisponding command for each binop command */
    // todo: how did mark say about not using switch? use map to map a binop.enum to a command?

    return null;
  }

  @Override
  public Register visitBoolNode(BoolNode node) {
    ARMConcreteRegister reg = armRegAllocator.allocate();
    Immediate immed = new Immediate(node.getVal() ? TRUE : FALSE, BitNum.SHIFT32);
    Operand2 operand2 = new Operand2(immed);
    // instructions.add(new Mov(reg, operand2));
    return reg;
  }

  @Override
  public Register visitCharNode(CharNode node) {
    ARMConcreteRegister reg = armRegAllocator.allocate();
    Immediate immed = new Immediate(node.getAsciiValue(), BitNum.SHIFT32);
    Operand2 operand2 = new Operand2(immed);
    // instructions.add(new Mov(reg, operand2));
    return reg;
  }

  @Override
  public Register visitIntegerNode(IntegerNode node) {
    // todo: same as visitCharNode
    return null;
  }

  @Override
  public Register visitFunctionCallNode(FunctionCallNode node) {
    // todo: sx119
    /* 1 compute parameters, all parameter in stack
    *    also add into function's identmap */
    for (ExprNode expr : node.getParams()) {
      visit(expr);
      // todo: use STR to store in stack, no need to change symbol table
    }

    /* 2 call function with B instruction */

    /* 3 get result, put in register */
    return null;
  }

  @Override
  public Register visitIdentNode(IdentNode node) {
    String identName = node.getName();
    /* if ident appear for the first time, return a new sudo reg */
    // if (identMap.containsKey(identName)) {
    //   return identMap.get(identName);
    // }
    /* new ident should be handled in declare node or related function node */
    throw new IllegalArgumentException("new Ident should be handled in visitDeclareNode or visitFuncNode, not in visitIdentNode");
  }

  @Override
  public Register visitPairElemNode(PairElemNode node) {
    /* TODO: xz1919 */
    return null;
  }

  @Override
  public Register visitPairNode(PairNode node) {
    /* TODO: xz1919 */
    return null;
  }

  @Override
  public Register visitStringNode(StringNode node) {
    /* TODO: xx1219 */
    return null;
  }

  @Override
  public Register visitUnopNode(UnopNode node) {
    // TODO: left over
    return null;
  }

  @Override
  public Register visitAssignNode(AssignNode node) {
    /* TODO: ss6919 */
    return null;
  }

  @Override
  public Register visitDeclareNode(DeclareNode node) {
    /* TODO: ss6919 */
    return null;
  }

  @Override
  public Register visitExitNode(ExitNode node) {
    /* TODO: xx1219 */
    return null;
  }

  @Override
  public Register visitFreeNode(FreeNode node) {
    /* TODO: xz1919 */
    return null;
  }

  @Override
  public Register visitIfNode(IfNode node) {
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
  public Register visitPrintlnNode(PrintlnNode node) {
    /* TODO: xx1219 */
    return null;
  }

  @Override
  public Register visitPrintNode(PrintNode node) {
    /* TODO: xx1219 */
    return null;
  }

  @Override
  public Register visitReadNode(ReadNode node) {
    /* TODO: xx1219 */
    return null;
  }

  @Override
  public Register visitReturnNode(ReturnNode node) {
    /* TODO: xz1919 */
    return null;
  }

  @Override
  public Register visitScopeNode(ScopeNode node) {
    // todo: sx119: reserve space for idents in stack
    List<StatNode> list = node.getBody();

    for (StatNode elem : list) {
      visit(elem);
    }
    return null;
  }

  @Override
  public Register visitSkipNode(SkipNode node) {
    return null;
  }

  @Override
  public Register visitWhileNode(WhileNode node) {
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
    /*   translate cond expr */
    visit(node.getCond());

    /* 5 conditional branch jump to the start of loop */
    new B(Cond.EQ, startLabel.toString());

    return null;
  }

  @Override
  public Register visitFuncNode(FuncNode node) {
    /* TODO: xz1919 */
    return null;
  }

  @Override
  public Register visitProgramNode(ProgramNode node) {
    /*  */
    return null;
  }
}
