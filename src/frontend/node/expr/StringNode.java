package frontend.node.expr;

import frontend.type.BasicType;
import frontend.type.BasicTypeEnum;

public class StringNode extends ExprNode {

  /**
   * Represent a string
   * Example: "hello, world!"
   */

  private final int length;

  public StringNode(String string) {
    this.length = string.length();
    this.type = new BasicType(BasicTypeEnum.STRING);
  }

  public int getLength() {
    return this.length;
  }
}
