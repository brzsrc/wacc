package node.stat;

import node.expr.ExprNode;

public class ReturnNode extends StatNode {

  /**
   * Represent the return statement in a non-main function
   * Example: return x, return 'a', return 1
   */

  private final ExprNode expr;

  public ReturnNode(ExprNode expr) {
    this.expr = expr;
    setLeaveAtEnd(true);
  }

}
