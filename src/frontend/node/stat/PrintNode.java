package frontend.node.stat;

import frontend.node.expr.ExprNode;
import frontend.visitor.NodeVisitor;

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
  public void accept(NodeVisitor visitor) {
    visitor.visitPrintNode(this);
  }
}
