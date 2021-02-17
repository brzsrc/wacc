package frontend.node.stat;

import frontend.node.expr.ExprNode;

public class DeclareNode extends StatNode {

  /**
   * Represent a declaration statement, with identifier and rhs recorded
   * Example: int x = 5, char c = '!', bool b = false
   */

  private final String identifier;
  private final ExprNode rhs;

  public DeclareNode(String identifier, ExprNode rhs) {
    this.identifier = identifier;
    this.rhs = rhs;
  }

  @Override
  public void showNode(int leadingSpace) {
    appendLeadingSpace(leadingSpace);
    rhs.getType().showType();
    System.out.print(" " + identifier + " = ");
    rhs.showNode(0);
    System.out.println();
  }
}
