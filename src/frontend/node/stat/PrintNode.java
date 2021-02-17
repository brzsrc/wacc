package frontend.node.stat;

import frontend.node.expr.ExprNode;

public class PrintNode extends StatNode {

  /**
   * Represent a print statement, with <expr> recorded
   * Example: print <expr>
   */

  private final ExprNode expr;

  public PrintNode(ExprNode expr) {
    this.expr = expr;
  }

  @Override
  public void showNode(int leadingSpace) {
    appendLeadingSpace(leadingSpace);
    System.out.print("print ");
    expr.showNode(0);
    System.out.println();
  }
}
