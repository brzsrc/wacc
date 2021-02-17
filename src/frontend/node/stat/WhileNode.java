package frontend.node.stat;

import frontend.node.expr.ExprNode;

public class WhileNode extends StatNode {

  /**
   * Represent a while-loop frontend.node, with condition and body recorded
   */

  private final ExprNode cond;
  private final StatNode body;

  public WhileNode(ExprNode cond, StatNode body) {
    this.cond = cond;
    this.body = body;
  }

}
