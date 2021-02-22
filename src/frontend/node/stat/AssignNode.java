package frontend.node.stat;

import frontend.node.expr.ExprNode;
import utils.NodeVisitor;

public class AssignNode extends StatNode {

  /**
   * Represent an assignment statement, with lhs and rhs recorded
   * Example: a = 4, b = true, c = 'p'
   */

  private final ExprNode lhs;
  private final ExprNode rhs;

  public AssignNode(ExprNode lhs, ExprNode rhs) {
    this.lhs = lhs;
    this.rhs = rhs;
  }

  public ExprNode getLhs() {
    return lhs;
  }

  public ExprNode getRhs() {
    return rhs;
  }

  @Override
  public <T> T accept(NodeVisitor<T> visitor) {
    return visitor.visitAssignNode(this);
  }
}
