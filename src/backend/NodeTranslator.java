package backend;

import backend.instructions.Instruction;
import backend.instructions.Mov;
import backend.instructions.Operand.Immediate;
import backend.instructions.Operand.Operand2;
import frontend.node.*;
import frontend.node.expr.*;
import frontend.node.stat.*;
import utils.NodeVisitor;
import utils.backend.PseudoRegister;
import utils.backend.PseudoRegisterAllocator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static backend.instructions.Operand.Immediate.BitNum;

public class NodeTranslator implements NodeVisitor<PseudoRegister> {

  public static final int TRUE = 1;
  public static final int FALSE = 0;
  private static PseudoRegisterAllocator pseudoRegisterAllocator;
  private static List<Instruction> instructions;

  public NodeTranslator() {
    pseudoRegisterAllocator = new PseudoRegisterAllocator();
    instructions = new ArrayList<>();
  }

  @Override
  public PseudoRegister visitArrayElemNode(ArrayElemNode node) {
    // todo: after discussing how to implement heap/stack allocation
    return null;
  }

  @Override
  public PseudoRegister visitArrayNode(ArrayNode node) {
    // todo: after discussing how to implement heap/stack allocation

    /* 1 generate size of array and put into r0 */

    /* 2 call malloc, get result from r4 */


    return null;
  }

  @Override
  public PseudoRegister visitBinopNode(BinopNode node) {
    PseudoRegister reg1 = visit(node.getExpr1());
    PseudoRegister reg2 = visit(node.getExpr2());

    /* generate corrisponding command for each binop command */
    // todo: how did mark say about not using switch? use map to map a binop.enum to a command?

    return null;
  }

  @Override
  public PseudoRegister visitBoolNode(BoolNode node) {
    PseudoRegister reg = pseudoRegisterAllocator.get();
    Immediate immed = new Immediate(node.getVal() ? TRUE : FALSE, BitNum.SHIFT32);
    Operand2 operand2 = new Operand2(immed);
    instructions.add(new Mov(reg, operand2));
    return reg;
  }

  @Override
  public PseudoRegister visitCharNode(CharNode node) {
    PseudoRegister reg = pseudoRegisterAllocator.get();
    Immediate immed = new Immediate(node.getAsciiValue(), BitNum.SHIFT32);
    Operand2 operand2 = new Operand2(immed);
    instructions.add(new Mov(reg, operand2));
    return reg;
  }

  @Override
  public PseudoRegister visitFunctionCallNode(FunctionCallNode node) {
    return null;
  }

  @Override
  public PseudoRegister visitIdentNode(IdentNode node) {
    String identName = node.getName();
    /* if ident appear for the first time, return a new sudo reg */
    // if (identMap.containsKey(identName)) {
    //   return identMap.get(identName);
    // }
    /* new ident should be handled in declare node or related function node */
    throw new IllegalArgumentException("new Ident should be handled in visitDeclareNode or visitFuncNode, not in visitIdentNode");
  }

  @Override
  public PseudoRegister visitIntegerNode(IntegerNode node) {
    // todo: same as visitCharNode
    return null;
  }

  @Override
  public PseudoRegister visitPairElemNode(PairElemNode node) {
    return null;
  }

  @Override
  public PseudoRegister visitPairNode(PairNode node) {
    return null;
  }

  @Override
  public PseudoRegister visitStringNode(StringNode node) {
    return null;
  }

  @Override
  public PseudoRegister visitUnopNode(UnopNode node) {
    // todo: same as binop
    return null;
  }

  @Override
  public PseudoRegister visitAssignNode(AssignNode node) {
    return null;
  }

  @Override
  public PseudoRegister visitDeclareNode(DeclareNode node) {
    return null;
  }

  @Override
  public PseudoRegister visitExitNode(ExitNode node) {
    return null;
  }

  @Override
  public PseudoRegister visitFreeNode(FreeNode node) {
    return null;
  }

  @Override
  public PseudoRegister visitIfNode(IfNode node) {
    return null;
  }

  @Override
  public PseudoRegister visitPrintlnNode(PrintlnNode node) {
    return null;
  }

  @Override
  public PseudoRegister visitPrintNode(PrintNode node) {
    return null;
  }

  @Override
  public PseudoRegister visitReadNode(ReadNode node) {
    return null;
  }

  @Override
  public PseudoRegister visitReturnNode(ReturnNode node) {
    return null;
  }

  @Override
  public PseudoRegister visitScopeNode(ScopeNode node) {
    return null;
  }

  @Override
  public PseudoRegister visitSkipNode(SkipNode node) {
    return null;
  }

  @Override
  public PseudoRegister visitWhileNode(WhileNode node) {
    return null;
  }

  @Override
  public PseudoRegister visitFuncNode(FuncNode node) {
    return null;
  }

  @Override
  public PseudoRegister visitProgramNode(ProgramNode node) {
    return null;
  }
}
