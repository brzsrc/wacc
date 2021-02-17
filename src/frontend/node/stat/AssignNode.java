package frontend.node.stat;

import frontend.node.expr.ExprNode;

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

  @Override
  public void showNode(int leadingSpace) {
    appendLeadingSpace(leadingSpace);
    lhs.showNode(0);
    System.out.print(" = ");
    rhs.showNode(0);
    System.out.println();
  }
}
