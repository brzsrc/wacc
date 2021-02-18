package frontend.node.stat;

import frontend.node.expr.ExprNode;
import frontend.visitor.NodeVisitor;

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
  public void accept(NodeVisitor visitor) {
    visitor.visitPrintlnNode(this);
  }
}
