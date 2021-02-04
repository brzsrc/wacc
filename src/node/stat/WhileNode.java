package node.stat;

import node.ExprNode;
import node.StatNode;

public class WhileNode implements StatNode {

  private final ExprNode cond;
  private final StatNode body;

  public WhileNode(ExprNode cond, StatNode body) {
    this.cond = cond;
    this.body = body;
  }
}
