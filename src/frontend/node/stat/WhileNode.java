package frontend.node.stat;

import frontend.node.expr.ExprNode;
import frontend.visitor.NodeVisitor;

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

  public ExprNode getCond() {
    return cond;
  }

  public StatNode getBody() {
    return body;
  }

  @Override
  public void accept(NodeVisitor visitor) {
    visitor.visitWhileNode(this);
  }
}
