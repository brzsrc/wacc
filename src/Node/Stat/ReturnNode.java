package Node.Stat;

import Node.Expr.ExprNode;

public class ReturnNode implements StatNode {

  private ExprNode returnExpr;

  public ReturnNode(ExprNode returnExpr) {
    this.returnExpr = returnExpr;
  }

  @Override
  public boolean hasReturn() {
    return true;
  }

  @Override
  public boolean hasEnd() {
    return true;
  }

}
