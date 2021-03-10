package frontend.node.expr;

import frontend.type.BasicType;
import frontend.type.BasicTypeEnum;
import utils.NodeVisitor;

public class BinopNode extends ExprNode {



  /**
   * Represent a binary operation node with a left and right expression a binary operation will
   * either have type int or bool
   *
   * Example: 1 + 2, 3 * 4, 5 + (6 * 2)
   */

  public enum Binop {
    PLUS, MINUS, MUL, DIV, MOD, GREATER, GREATER_EQUAL, LESS, LESS_EQUAL, EQUAL, INEQUAL, AND, OR
  }

  private ExprNode expr1;
  private ExprNode expr2;
  private Binop operator;

  public BinopNode(ExprNode expr1, ExprNode expr2, Binop operator) {
    this.expr1 = expr1;
    this.expr2 = expr2;
    this.operator = operator;
    switch (operator) {
      case PLUS:
      case MINUS:
      case MUL:
      case DIV:
      case MOD:
        type = new BasicType(BasicTypeEnum.INTEGER);
        break;
      default:
        type = new BasicType(BasicTypeEnum.BOOLEAN);
    }
    weight = expr1.getWeight() + expr2.getWeight() + 2;
  }

  public ExprNode getExpr1() {
    return expr1;
  }

  public void setExpr1(ExprNode expr1) {
    this.expr1 = expr1;
  }

  public ExprNode getExpr2() {
    return expr2;
  }

  public void setExpr2(ExprNode expr2) {
    this.expr2 = expr2;
  }

  public Binop getOperator() {
    return operator;
  }

  public void setOperator(Binop operator) {
    this.operator = operator;
  }

  @Override
  public <T> T accept(NodeVisitor<T> visitor) {
    return visitor.visitBinopNode(this);
  }

  /* should not overwrite is immediate,
   * since even if both child are immediate, it means evaluator failed to simplify it,
   * calling getCast val on it will cause error */
}
