package node.stat;

import node.expr.ExprNode;

public class ExitNode extends StatNode {

  private final ExprNode value;

  public ExitNode(ExprNode value) {
    this.value = value;
    setAll();
  }

  @Override
  public void setLeaveAtEnd() {
    leaveAtEnd = true;
  }

}
