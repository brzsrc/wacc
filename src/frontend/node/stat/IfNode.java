package frontend.node.stat;

import frontend.node.expr.ExprNode;
import frontend.visitor.NodeVisitor;

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

  public ExprNode getCond() {
    return cond;
  }

  public StatNode getIfBody() {
    return ifBody;
  }

  public StatNode getElseBody() {
    return elseBody;
  }

  @Override
  public void accept(NodeVisitor visitor) {
    visitor.visitIfNode(this);
  }
}
