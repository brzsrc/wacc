package frontend.node.stat;

import frontend.node.expr.ExprNode;
import utils.NodeVisitor;

public class PrintlnNode extends StatNode {

  /**
   * Represent the println statement, with <expr> being recorded
   * Example: println <expr>
   */

  private final ExprNode expr;

  public PrintlnNode(ExprNode expr) {
    this.expr = expr;
  }

  public ExprNode getExpr() {
    return expr;
  }

  @Override
  public <T> T accept(NodeVisitor<T> visitor) {
    return visitor.visitPrintlnNode(this);
  }
}
