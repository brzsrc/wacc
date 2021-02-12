package node.stat;

import node.expr.ExprNode;
import type.Type;

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

}
