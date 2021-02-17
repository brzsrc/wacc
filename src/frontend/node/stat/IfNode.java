package frontend.node.stat;

import frontend.node.expr.ExprNode;

public class IfNode extends StatNode {

  /**
   * Represent an if-else statement, with condition and if-body, else-body recorded
   */

  private final ExprNode cond;
  private final StatNode ifBody;
  private final StatNode elseBody;

  public IfNode(ExprNode cond, StatNode ifBody, StatNode elseBody) {
    this.cond = cond;
    this.ifBody = ifBody;
    this.elseBody = elseBody;
    setLeaveAtEnd(getEndValue());
  }


  private boolean getEndValue() {
    assert ifBody != null && elseBody != null;
    return ifBody.leaveAtEnd() && elseBody.leaveAtEnd();
  }

  @Override
  public void showNode(int leadingSpace) {
    /* if EXPR : */
    appendLeadingSpace(leadingSpace);
    System.out.print("if ");
    cond.showNode(0);
    System.out.println(" :");

    /* show if body */
    ifBody.showNode(leadingSpace + INDENT_SIZE);

    /* else */
    appendLeadingSpace(leadingSpace);
    System.out.println("else");

    /* show else body */
    elseBody.showNode(leadingSpace + INDENT_SIZE);

    /*\n */
    appendLeadingSpace(leadingSpace);
    System.out.println();
  }
}
