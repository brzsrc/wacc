package frontend.node.expr;

import frontend.type.BasicType;
import frontend.type.BasicTypeEnum;

public class IntegerNode extends ExprNode {

  /**
   * Represent the integer frontend.node
   * Example: 10, 25, 37
   */

  private final int val;

  public IntegerNode(int val) {
    this.val = val;
    this.type = new BasicType(BasicTypeEnum.INTEGER);
  }
}
