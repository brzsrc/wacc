package frontend.node.expr;

import frontend.type.BasicType;
import frontend.type.BasicTypeEnum;

public class StringNode extends ExprNode {

  /**
   * Represent a string
   * Example: "hello, world!"
   */

  private final String string;

  public StringNode(String string) {
    this.string = string;
    this.type = new BasicType(BasicTypeEnum.STRING);
  }

  public int getLength() {
    return this.string.length();
  }

  @Override
  public void showNode(int leadingSpace) {
    System.out.print(string);
  }
}
