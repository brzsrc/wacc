package node.stat;

import node.expr.ExprNode;

public class PrintNode extends StatNode {

  private final ExprNode value;

  public PrintNode(ExprNode value) {
    this.value = value;
  }
}
