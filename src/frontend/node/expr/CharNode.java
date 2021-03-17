package frontend.node.expr;

import frontend.type.BasicType;
import frontend.type.BasicTypeEnum;
import utils.NodeVisitor;
import utils.Utils.AssemblyArchitecture;

public class CharNode extends ExprNode {

  /**
   * Represent a character node
   * Example: 'a', '!', '?'
   */

  private final char val;

  public CharNode(char c, AssemblyArchitecture arch) {
    this.val = c;
    this.type = new BasicType(BasicTypeEnum.CHAR, arch);
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
  public int getCastedVal() {
    return getAsciiValue();
  }
}
