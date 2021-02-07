package node.stat;

import node.expr.ExprNode;

public class ReadNode extends StatNode {
  private ExprNode readTarget;

  public ReadNode(ExprNode readTarget) {
    this.readTarget = readTarget;
  }


}
