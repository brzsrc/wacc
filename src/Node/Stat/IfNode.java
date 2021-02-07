package Node.Stat;

import Node.Expr.ExprNode;

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
  public void setLeaveAtEnd() {
    assert ifBody != null && elseBody != null;
    leaveAtEnd = ifBody.isLeaveAtEnd() && elseBody.isLeaveAtEnd();
  }

  @Override
  public void setHasReturn() {
    assert ifBody != null && elseBody != null;
    hasReturn = ifBody.isHasReturn() || elseBody.isHasReturn();
  }
}
