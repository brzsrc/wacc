package frontend.node.stat;

import frontend.node.expr.ExprNode;
import utils.NodeVisitor;

public class WhileNode extends StatNode {

  /**
   * Represent a while-loop or a do-while node, with condition and body recorded
   */

  private final ExprNode cond;
  private final StatNode body;
  private final boolean isDoWhile;

  public WhileNode(ExprNode cond, StatNode body, boolean isDoWhile) {
    this.cond = cond;
    this.body = body;
    this.isDoWhile = isDoWhile;
    this.minStackSpace = body.minStackSpace;
  }

  public WhileNode(ExprNode cond, StatNode body) {
    this(cond, body, false);
  }

  public ExprNode getCond() {
    return cond;
  }

  public StatNode getBody() {
    return body;
  }

  public boolean isDoWhile() {
    return this.isDoWhile;
  }

  @Override
  public <T> T accept(NodeVisitor<T> visitor) {
    return visitor.visitWhileNode(this);
  }
}
