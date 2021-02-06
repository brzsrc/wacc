package utils.IR.CFG.stat;

import Node.Stat.ExprNode;
import Node.StatNode;

public class IfNode implements StatNode {

  private final ExprNode cond;
  private final ScopeNode ifBody;
  private final ScopeNode elseBody;

  public IfNode(ExprNode cond, ScopeNode ifBody, ScopeNode elseBody) {
    this.cond = cond;
    this.ifBody = ifBody;
    this.elseBody = elseBody;
  }

  @Override
  public boolean hasEnd() {
    return ifBody.hasEnd() && elseBody.hasEnd();
  }
}
