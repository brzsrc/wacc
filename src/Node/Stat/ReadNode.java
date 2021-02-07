package Node.Stat;

import Node.Expr.ExprNode;

public class ReadNode extends StatNode {
  private ExprNode readTarget;

  public ReadNode(ExprNode readTarget) {
    this.readTarget = readTarget;
  }


}
