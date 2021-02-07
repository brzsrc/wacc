package Node.Stat;

import Node.Expr.ExprNode;

public class ReadNode implements StatNode {
  private ExprNode readTarget;

  public ReadNode(ExprNode readTarget) {
    this.readTarget = readTarget;
  }


}
