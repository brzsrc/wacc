package node.stat;

import node.expr.ExprNode;

public class PrintNode extends StatNode {

  /**
   * Represent a print statement, with <expr> recorded
   * Example: print <expr>
   */

  private final ExprNode expr;

  public PrintNode(ExprNode expr) {
    this.expr = expr;
  }
}
