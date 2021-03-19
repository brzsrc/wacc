package frontend.node.stat;

import frontend.node.expr.ExprNode;
import utils.NodeVisitor;

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
    if (elseBody == null) {
      this.minStackSpace = ifBody.minStackSpace;
    } else {
      this.minStackSpace = Math.max(ifBody.minStackSpace, elseBody.minStackSpace);
    }
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
  public <T> T accept(NodeVisitor<T> visitor) {
    return visitor.visitIfNode(this);
  }
}
