package Node.Stat;

import Node.Expr.ExprNode;

public class ExitNode implements StatNode {

  private ExprNode exitExpr;

  public ExitNode(ExprNode exitExpr) {
    this.exitExpr = exitExpr;
  }


  @Override
  public boolean hasEnd() {
    return true;
  }

}
