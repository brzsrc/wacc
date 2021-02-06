package Node.Stat;

import Node.Expr.ExprNode;

public class WhileNode implements StatNode {

  private final ExprNode cond;
  private final ScopeNode body;

  public WhileNode(ExprNode cond, ScopeNode body) {
    this.cond = cond;
    this.body = body;
  }
}
