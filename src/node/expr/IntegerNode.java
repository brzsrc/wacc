package node.expr;

import type.BasicType;
import type.BasicTypeEnum;

public class IntegerNode extends ExprNode {

  private int val;

  public IntegerNode(int val) {
    super("");
    this.val = val;
    this.type = new BasicType(BasicTypeEnum.INTEGER);
  }
}
