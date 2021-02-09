package node.stat;

import node.expr.ExprNode;

public class IfNode extends StatNode {

  private final ExprNode cond;
  private final StatNode ifBody;
  private final StatNode elseBody;

  public IfNode(ExprNode cond, StatNode ifBody, StatNode elseBody) {
    this.cond = cond;
    this.ifBody = ifBody;
    this.elseBody = elseBody;
    setAll();
  }

  @Override
  protected void setLeaveAtEnd() {
    assert ifBody != null && elseBody != null;
    leaveAtEnd = ifBody.leaveAtEnd() && elseBody.leaveAtEnd();
  }

  @Override
  protected void setHasReturn() {
    assert ifBody != null && elseBody != null;
    hasReturn = ifBody.hasReturn() || elseBody.hasReturn();
  }
}
