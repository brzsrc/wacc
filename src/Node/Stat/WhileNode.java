package Node.Stat;

import Node.Expr.ExprNode;

public class WhileNode extends StatNode {

  private final ExprNode cond;
  private final StatNode body;

  public WhileNode(ExprNode cond, StatNode body) {
    this.cond = cond;
    this.body = body;
    setAll();
  }

  @Override
  public void setHasReturn() {
    assert body != null;
    hasReturn = body.isHasReturn();
  }
}
