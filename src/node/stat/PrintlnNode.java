package node.stat;

import node.expr.ExprNode;

public class PrintlnNode extends StatNode {

  private final ExprNode value;

  public PrintlnNode(ExprNode value) {
    this.value = value;
  }
}
