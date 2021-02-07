package node.stat;

import node.expr.ExprNode;

public class ReturnNode extends StatNode {

  private final ExprNode value;

  public ReturnNode(ExprNode value) {
    this.value = value;
    setAll();
  }

  @Override
  public void setHasReturn() {
    hasReturn = true;
  }

  @Override
  public void setLeaveAtEnd() {
    leaveAtEnd = true;
  }

}
