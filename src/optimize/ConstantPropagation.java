package optimize;

import frontend.node.FuncNode;
import frontend.node.Node;
import frontend.node.ProgramNode;
import frontend.node.expr.*;
import frontend.node.stat.*;
import utils.NodeVisitor;

import static utils.Utils.*;

public class ConstantPropagation implements NodeVisitor<Node> {

  @Override
  public Node visitArrayElemNode(ArrayElemNode node) {
    return null;
  }

  @Override
  public Node visitArrayNode(ArrayNode node) {
    return null;
  }

  @Override
  public Node visitBinopNode(BinopNode node) {
    ExprNode expr1 = node.getExpr1();
    ExprNode expr2 = node.getExpr2();
    if (!expr1.isImmediate() || !expr2.isImmediate()) {
      return node;
    }

    /* if one expression is integer, then binop is arithmetic evaluation */
    if (arithmeticApplyMap.containsKey(node.getOperator())) {
      int val = arithmeticApplyMap.get(node.getOperator()).apply(
              expr1.asIntegerNode().getVal(),
              expr2.asIntegerNode().getVal());
      return new IntegerNode(val);
    }

    /* if expression is  */
    if (arithmeticCmpMap.containsKey(node.getOperator())) {
      boolean val = arithmeticCmpMap.get(node.getOperator()).apply(
              expr1.asIntegerNode().getVal(),
              expr2.asIntegerNode().getVal());

      return new BoolNode(val);
    }

    assert booleanCmpMap.containsKey(node.getOperator());

    boolean val = booleanCmpMap.get(node.getOperator()).apply(
            expr1.asBoolNode().getVal(),
            expr2.asBoolNode().getVal());
    return new BoolNode(val);
  }

  @Override
  public Node visitBoolNode(BoolNode node) {
    return node;
  }

  @Override
  public Node visitCharNode(CharNode node) {
    return node;
  }

  @Override
  public Node visitFunctionCallNode(FunctionCallNode node) {
    return null;
  }

  @Override
  public Node visitIdentNode(IdentNode node) {
    return null;
  }

  @Override
  public Node visitIntegerNode(IntegerNode node) {
    return null;
  }

  @Override
  public Node visitPairElemNode(PairElemNode node) {
    return null;
  }

  @Override
  public Node visitPairNode(PairNode node) {
    return null;
  }

  @Override
  public Node visitStringNode(StringNode node) {
    return null;
  }

  @Override
  public Node visitUnopNode(UnopNode node) {

    return null;
  }

  @Override
  public Node visitAssignNode(AssignNode node) {
    return null;
  }

  @Override
  public Node visitDeclareNode(DeclareNode node) {
    return null;
  }

  @Override
  public Node visitExitNode(ExitNode node) {
    return null;
  }

  @Override
  public Node visitFreeNode(FreeNode node) {
    return null;
  }

  @Override
  public Node visitIfNode(IfNode node) {
    return null;
  }

  @Override
  public Node visitPrintlnNode(PrintlnNode node) {
    return null;
  }

  @Override
  public Node visitPrintNode(PrintNode node) {
    return null;
  }

  @Override
  public Node visitReadNode(ReadNode node) {
    return null;
  }

  @Override
  public Node visitReturnNode(ReturnNode node) {
    return null;
  }

  @Override
  public Node visitScopeNode(ScopeNode node) {
    return null;
  }

  @Override
  public Node visitSkipNode(SkipNode node) {
    return null;
  }

  @Override
  public Node visitWhileNode(WhileNode node) {
    return null;
  }

  @Override
  public Node visitFuncNode(FuncNode node) {
    return null;
  }

  @Override
  public Node visitProgramNode(ProgramNode node) {
    return null;
  }
}
