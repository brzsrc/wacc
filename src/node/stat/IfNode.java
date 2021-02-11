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
    setLeaveAtEnd(getEndValue());
  }


  private boolean getEndValue() {
    assert ifBody != null && elseBody != null;
    return ifBody.leaveAtEnd() && elseBody.leaveAtEnd();
  }

}
