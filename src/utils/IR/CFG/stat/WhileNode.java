package utils.IR.CFG.stat;

import utils.IR.CFG.ExprNode;
import utils.IR.CFG.StatNode;

public class WhileNode implements StatNode {

  private final ExprNode cond;
  private final ScopeNode body;

  public WhileNode(ExprNode cond, ScopeNode body) {
    this.cond = cond;
    this.body = body;
  }
}
