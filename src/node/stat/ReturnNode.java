package node.stat;

import node.expr.ExprNode;
import type.Type;

public class ReturnNode extends StatNode {

  private final ExprNode value;

  public ReturnNode(ExprNode value) {
    this.value = value;
    setAll();
  }

  @Override
  protected void setHasReturn() {
    hasReturn = true;
  }

  @Override
  protected void setLeaveAtEnd() {
    leaveAtEnd = true;
  }

}
