package frontend.node.stat;

import frontend.node.expr.ExprNode;

public class WhileNode extends StatNode {

  /**
   * Represent a while-loop node, with condition and body recorded
   */

  private final ExprNode cond;
  private final StatNode body;

  public WhileNode(ExprNode cond, StatNode body) {
    this.cond = cond;
    this.body = body;
  }

  @Override
  public void showNode(int leadingSpace) {
    /* while COND : */
    appendLeadingSpace(leadingSpace);
    System.out.print("while ");
    cond.showNode(0);
    System.out.println(" :");

    /* body */
    body.showNode(leadingSpace + INDENT_SIZE);

  }
}
