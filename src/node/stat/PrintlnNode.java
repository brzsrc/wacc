package node.stat;

import node.expr.ExprNode;

public class PrintlnNode extends StatNode {

  /**
   * Represent the println statement, with <expr> being recorded
   * Example: println <expr>
   */

  private final ExprNode expr;

  public PrintlnNode(ExprNode expr) {
    this.expr = expr;
  }
}
