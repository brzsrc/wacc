package node.stat;

import node.expr.ExprNode;
import type.Type;

public class ExitNode extends StatNode {

  private final ExprNode value;

  public ExitNode(ExprNode value) {
    this.value = value;
    setLeaveAtEnd(true);
  }

}
