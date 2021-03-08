package frontend.node.expr;

import com.sun.security.jgss.GSSUtil;
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
    System.out.println("in is imm" + this.getClass());
    return false;
  }

  /* overwrite by Char, Integer, Bool nodes,
  *  char return ascii num,
  *  int return value
  *  bool return 1 if true, 0 if false */
  public int getCastedVal() {
    throw new IllegalArgumentException("calling getVal on type not immediate");
  }

}
