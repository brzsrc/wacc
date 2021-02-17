package frontend.node.stat;

import frontend.node.expr.ExprNode;

public class PrintlnNode extends StatNode {

  /**
   * Represent the println statement, with <expr> being recorded
   * Example: println <expr>
   */

  private final ExprNode expr;

  public PrintlnNode(ExprNode expr) {
    this.expr = expr;
  }

  @Override
  public void showNode(int leadingSpace) {
    appendLeadingSpace(leadingSpace);
    System.out.print("println ");
    expr.showNode(0);
    System.out.println();
  }
}
