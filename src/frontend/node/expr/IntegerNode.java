package frontend.node.expr;

import frontend.type.BasicType;
import frontend.type.BasicTypeEnum;
import frontend.visitor.NodeVisitor;

public class IntegerNode extends ExprNode {

  /**
   * Represent the integer node
   * Example: 10, 25, 37
   */

  private final int val;

  public IntegerNode(int val) {
    this.val = val;
    this.type = new BasicType(BasicTypeEnum.INTEGER);
  }

  public int getVal() {
    return val;
  }

  @Override
  public void accept(NodeVisitor visitor) {
    visitor.visitIntegerNode(this);
  }
}
