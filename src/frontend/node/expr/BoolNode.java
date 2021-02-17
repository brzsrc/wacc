package frontend.node.expr;

import frontend.type.BasicType;
import frontend.type.BasicTypeEnum;

public class BoolNode extends ExprNode {

  /**
   * Represent a boolean node
   * Example: true, false
   */

  private final boolean val;

  public BoolNode(boolean val) {
    this.val = val;
    this.type = new BasicType(BasicTypeEnum.BOOLEAN);
  }

  @Override
  public void showNode() {
    System.out.println(val);
  }

}
