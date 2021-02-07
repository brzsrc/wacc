package Node.Stat;

import Node.Expr.ExprNode;

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

  @Override
  public boolean hasReturn() {
    return ifBody.hasReturn() || elseBody.hasReturn();
  }
}
