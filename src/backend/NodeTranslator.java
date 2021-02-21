package backend;

import backend.instructions.Instruction;
import backend.instructions.Mov;
import backend.instructions.Operand.Immediate;
import backend.instructions.Operand.SudoRegister;
import frontend.node.*;
import frontend.node.expr.*;
import frontend.node.stat.*;
import frontend.visitor.NodeVisitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static backend.instructions.Operand.Immediate.BitNum;
import static backend.instructions.Operand.SudoRegister.getCurrAvailableReg;
import static backend.instructions.Operand.SudoRegister.peakLastReg;

public class NodeTranslator implements NodeVisitor<SudoRegister> {

  public static final int TRUE = 1;
  public static final int FALSE = 0;
  private static Map<String, SudoRegister> identMap;
  private static List<Instruction> instructions;

  public NodeTranslator() {
    identMap = new HashMap<>();
    instructions = new ArrayList<>();
  }

  @Override
  public SudoRegister visitArrayElemNode(ArrayElemNode node) {
    // todo: after discussing how to implement heap/stack allocation
    return null;
  }

  @Override
  public SudoRegister visitArrayNode(ArrayNode node) {
    // todo: after discussing how to implement heap/stack allocation

    /* 1 generate size of array and put into r0 */

    /* 2 call malloc, get result from r4 */


    return null;
  }

  @Override
  public SudoRegister visitBinopNode(BinopNode node) {
    SudoRegister reg1 = visit(node.getExpr1());
    SudoRegister reg2 = visit(node.getExpr2());

    /* generate corrisponding command for each binop command */
    // todo: how did mark say about not using switch? use map to map a binop.enum to a command?

    return null;
  }

  @Override
  public SudoRegister visitBoolNode(BoolNode node) {
    SudoRegister reg = getCurrAvailableReg();
    instructions.add(new Mov(reg, new Immediate(node.getVal() ? TRUE : FALSE, BitNum.SHIFT32)));
    return reg;
  }

  @Override
  public SudoRegister visitCharNode(CharNode node) {
    SudoRegister reg = getCurrAvailableReg();
    instructions.add(new Mov(reg, new Immediate(node.getAsciiValue(), BitNum.SHIFT32)));
    return reg;
  }

  @Override
  public SudoRegister visitFunctionCallNode(FunctionCallNode node) {
    return null;
  }

  @Override
  public SudoRegister visitIdentNode(IdentNode node) {
    String identName = node.getName();
    /* if ident appear for the first time, return a new sudo reg */
    if (identMap.containsKey(identName)) {
      return identMap.get(identName);
    }
    /* new ident should be handled in declare node or related function node */
    throw new IllegalArgumentException("new Ident should be handled in visitDeclareNode or visitFuncNode, not in visitIdentNode");
  }

  @Override
  public SudoRegister visitIntegerNode(IntegerNode node) {
    // todo: same as visitCharNode
    return null;
  }

  @Override
  public SudoRegister visitPairElemNode(PairElemNode node) {
    return null;
  }

  @Override
  public SudoRegister visitPairNode(PairNode node) {
    return null;
  }

  @Override
  public SudoRegister visitStringNode(StringNode node) {
    return null;
  }

  @Override
  public SudoRegister visitUnopNode(UnopNode node) {
    // todo: same as binop
    return null;
  }

  @Override
  public SudoRegister visitAssignNode(AssignNode node) {
    return null;
  }

  @Override
  public SudoRegister visitDeclareNode(DeclareNode node) {
    return null;
  }

  @Override
  public SudoRegister visitExitNode(ExitNode node) {
    return null;
  }

  @Override
  public SudoRegister visitFreeNode(FreeNode node) {
    return null;
  }

  @Override
  public SudoRegister visitIfNode(IfNode node) {
    return null;
  }

  @Override
  public SudoRegister visitPrintlnNode(PrintlnNode node) {
    return null;
  }

  @Override
  public SudoRegister visitPrintNode(PrintNode node) {
    return null;
  }

  @Override
  public SudoRegister visitReadNode(ReadNode node) {
    return null;
  }

  @Override
  public SudoRegister visitReturnNode(ReturnNode node) {
    return null;
  }

  @Override
  public SudoRegister visitScopeNode(ScopeNode node) {
    return null;
  }

  @Override
  public SudoRegister visitSkipNode(SkipNode node) {
    return null;
  }

  @Override
  public SudoRegister visitWhileNode(WhileNode node) {
    return null;
  }

  @Override
  public SudoRegister visitFuncNode(FuncNode node) {
    return null;
  }

  @Override
  public SudoRegister visitProgramNode(ProgramNode node) {
    return null;
  }
}
