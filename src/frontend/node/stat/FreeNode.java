package frontend.node.stat;

import frontend.node.expr.ExprNode;

public class FreeNode extends StatNode {

  /**
   * Represent a free statement, with <expr> being recorded
   * Example: free <expr>, free p (where p is a non-null pair)
   */

  private final ExprNode expr;

  public FreeNode(ExprNode expr) {
    this.expr = expr;
  }
}
