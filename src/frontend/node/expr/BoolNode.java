package frontend.node.expr;

import frontend.type.BasicType;
import frontend.type.BasicTypeEnum;
import frontend.visitor.NodeVisitor;

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

  public boolean getVal() {
    return val;
  }

  @Override
  public <T> T accept(NodeVisitor<T> visitor) {
    return visitor.visitBoolNode(this);
  }

}
