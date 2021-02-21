package frontend.node.stat;

import frontend.node.expr.ExprNode;
import utils.NodeVisitor;

public class PrintNode extends StatNode {

  /**
   * Represent a print statement, with <expr> recorded
   * Example: print <expr>
   */

  private final ExprNode expr;

  public PrintNode(ExprNode expr) {
    this.expr = expr;
  }

  public ExprNode getExpr() {
    return expr;
  }

  @Override
  public <T> T accept(NodeVisitor<T> visitor) {
    return visitor.visitPrintNode(this);
  }
}
