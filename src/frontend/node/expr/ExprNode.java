package frontend.node.expr;

import frontend.node.Node;
import frontend.type.Type;

public abstract class ExprNode implements Node {

  /**
   * abstract class for expression nodes. Every expression frontend.node has a frontend.type
   */

  protected Type type;

  public Type getType() {
    return this.type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  @Override
  public ExprNode asExprNode() {
    return this;
  }
}
