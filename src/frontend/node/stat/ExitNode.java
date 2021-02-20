package frontend.node.stat;

import frontend.node.expr.ExprNode;
import frontend.visitor.NodeVisitor;

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

  public ExprNode getValue() {
    return value;
  }

  @Override
  public <T> T accept(NodeVisitor<T> visitor) {
    return visitor.visitExitNode(this);
  }
}
