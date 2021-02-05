package utils.IR.CFG.stat;

import utils.IR.CFG.ExprNode;
import utils.IR.CFG.StatNode;

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
