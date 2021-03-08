package frontend.node.expr;

import frontend.type.BasicType;
import frontend.type.BasicTypeEnum;
import utils.NodeVisitor;

public class CharNode extends ExprNode {

  /**
   * Represent a character node
   * Example: 'a', '!', '?'
   */

  private final char val;

  public CharNode(char c) {
    this.val = c;
    this.type = new BasicType(BasicTypeEnum.CHAR);
    this.weight = 1;
  }

  public int getAsciiValue() {
    return val;
  }

  @Override
  public <T> T accept(NodeVisitor<T> visitor) {
    return visitor.visitCharNode(this);
  }

  @Override
  public boolean isImmediate() {
    return true;
  }

  @Override
  public CharNode asCharNode() {
    return this;
  }
}
