package frontend.node.stat;

import frontend.node.expr.ExprNode;
import frontend.visitor.NodeVisitor;

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
  public void accept(NodeVisitor visitor) {
    visitor.visitReadNode(this);
  }
}
