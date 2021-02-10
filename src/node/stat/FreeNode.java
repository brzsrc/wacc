package node.stat;

import node.expr.ExprNode;

public class FreeNode extends StatNode {

  private final ExprNode value;

  public FreeNode(ExprNode value) {
    this.value = value;
  }
}
