package frontend.node.expr;

import frontend.type.BasicType;
import frontend.type.BasicTypeEnum;
import utils.NodeVisitor;

public class BoolNode extends ExprNode {

  /**
   * Represent a boolean node
   * Example: true, false
   */

  private final boolean val;

  public BoolNode(boolean val) {
    this.val = val;
    this.type = new BasicType(BasicTypeEnum.BOOLEAN);
    this.weight = 1;
  }

  public boolean getVal() {
    return val;
  }

  @Override
  public <T> T accept(NodeVisitor<T> visitor) {
    return visitor.visitBoolNode(this);
  }

  @Override
  public boolean isImmediate() {
    return true;
  }

  @Override
  public int getCastedVal() {
    return val ? 1 : 0;
  }
}
