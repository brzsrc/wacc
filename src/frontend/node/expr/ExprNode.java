package frontend.node.expr;

import frontend.node.Node;
import frontend.type.Type;

public abstract class ExprNode implements Node {

  /**
   * abstract class for expression nodes. Every expression node has a type
   */

  protected Type type;
  protected int weight = 0;

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

  public int getWeight() {
    return weight;
  }

  /**
   *  functions for optimisations
   */

  /* if the expr contain value that does not depend on variable
  *  i.e if expr is int, char, bool*/
  public boolean isImmediate() {
    return false;
  }

  public IntegerNode asIntegerNode() {
    throw new IllegalArgumentException("casting exprNode to IntegerNode, origin type" + this.getClass().getName());
  }

  public CharNode asCharNode() {
    throw new IllegalArgumentException("casting exprNode to CharNode, origin type" + this.getClass().getName());
  }

  public BoolNode asBoolNode() {
    throw new IllegalArgumentException("casting exprNode to BoolNode, origin type" + this.getClass().getName());
  }

}
