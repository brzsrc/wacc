package frontend.node.stat;

import frontend.node.expr.ExprNode;

public class ReadNode extends StatNode {

  /**
   * Represent a read statement, with the target being recorded
   * Example: read x
   */

  private final ExprNode readTarget;

  public ReadNode(ExprNode readTarget) {
    this.readTarget = readTarget;
  }

  public ExprNode getInputExpr() {
    return readTarget;
  }

  @Override
  public void showNode(int leadingSpace) {
    appendLeadingSpace(leadingSpace);
    System.out.print("read ");
    readTarget.showNode(0);
    System.out.println();
  }
}
