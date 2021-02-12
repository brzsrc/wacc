package node.expr;

import type.BasicType;
import type.BasicTypeEnum;

public class CharNode extends ExprNode {

  /**
   * Represent a character node
   * Example: 'a', '!', '?'
   */

  private final char val;

  public CharNode(char c) {
    this.val = c;
    this.type = new BasicType(BasicTypeEnum.CHAR);
  }

  public int getAsciiValue() {
    return val;
  }

}
