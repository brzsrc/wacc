package frontend.node.stat;

import frontend.node.expr.ExprNode;

public class ExitNode extends StatNode {

  /**
   * Represent an exit statement, with the exit code recorded
   * Example: exit -1
   */

  private final ExprNode value;

  public ExitNode(ExprNode value) {
    this.value = value;
    setLeaveAtEnd(true);
  }

  @Override
  public void showNode(int leadingSpace) {
    appendLeadingSpace(leadingSpace);
    System.out.print("exit ");
    value.showNode(0);
    System.out.println();
  }
}
