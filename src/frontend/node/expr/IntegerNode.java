package frontend.node.expr;

import frontend.type.BasicType;
import frontend.type.BasicTypeEnum;
import utils.NodeVisitor;

public class IntegerNode extends ExprNode {

  /**
   * Represent the integer node
   * Example: 10, 25, 37
   */

  private final int val;

  public IntegerNode(int val) {
    this.val = val;
    this.type = new BasicType(BasicTypeEnum.INTEGER);
    this.weight = 1;
  }

  public int getVal() {
    return val;
  }

  @Override
  public <T> T accept(NodeVisitor<T> visitor) {
    return visitor.visitIntegerNode(this);
  }

  @Override
  public IntegerNode asIntegerNode() {
    return this;
  }
}
