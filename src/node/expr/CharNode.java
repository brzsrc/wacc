package node.expr;

import type.BasicType;
import type.BasicTypeEnum;

public class CharNode extends ExprNode {

  private char val;

  public CharNode(char c) {
    super(String.valueOf(c));
    this.val = c;
    this.type = new BasicType(BasicTypeEnum.CHAR);
  }

  public int getAsciiValue() {
    return val;
  }

}
