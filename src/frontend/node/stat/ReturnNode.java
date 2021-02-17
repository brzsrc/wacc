package frontend.node.stat;

import frontend.node.expr.ExprNode;

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

  @Override
  public void showNode(int leadingSpace) {
    appendLeadingSpace(leadingSpace);
    System.out.print("return ");
    expr.showNode(0);
    System.out.println();
  }
}
