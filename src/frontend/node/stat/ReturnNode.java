package frontend.node.stat;

import frontend.node.expr.ExprNode;
import frontend.visitor.NodeVisitor;

public class ReturnNode extends StatNode {

  /**
   * Represent the return statement in a non-main function
   * Example: return x, return 'a', return 1
   */

  private final ExprNode expr;

  public ReturnNode(ExprNode expr) {
    this.expr = expr;
    setLeaveAtEnd(true);
  }

  public ExprNode getExpr() {
    return expr;
  }

  @Override
  public <T> T accept(NodeVisitor<T> visitor) {
    return visitor.visitReturnNode(this);
  }
}
