package backend;

import backend.instructions.BL;
import backend.instructions.Instruction;
import backend.instructions.Mov;
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

  public ARMInstructionGenerator() {
    pseudoRegAllocator = new PseudoRegisterAllocator();
    armRegAllocator = new ARMConcreteRegisterAllocator();
    instructions = new ArrayList<>();
    identRegMap = new HashMap<>();
  }

  @Override
  public Register visitArrayElemNode(ArrayElemNode node) {
    /* TODO: xz1919 */
    /* 1 generate index out off bound checker function, error message */

    /* 2 put result in register */

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
    /* TODO: write function call together maybe, sx119 */

    /* 1 compute parameters, add into identmap
    *    each parameter's name define as */
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
    /* TODO: sx119 */
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
    /* TODO: sx119 */
    return null;
  }

  @Override
  public Register visitSkipNode(SkipNode node) {
    return null;
  }

  @Override
  public Register visitWhileNode(WhileNode node) {
    /* TODO: sx119 */
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
