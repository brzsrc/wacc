package frontend.node.expr;

import frontend.type.BasicType;
import frontend.type.BasicTypeEnum;

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

  @Override
  public void showNode(int leadingSpace) {
    System.out.print(val);
  }
}
