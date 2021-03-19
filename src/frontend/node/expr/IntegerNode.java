package frontend.node.expr;

import frontend.type.BasicType;
import frontend.type.BasicTypeEnum;
import utils.NodeVisitor;
import utils.Utils.AssemblyArchitecture;

public class IntegerNode extends ExprNode {

  /**
   * Represent the integer node
   * Example: 10, 25, 37
   */

  private final int val;

  public IntegerNode(int val, AssemblyArchitecture arch) {
    this.val = val;
    this.type = new BasicType(BasicTypeEnum.INT, arch);
    this.weight = 1;
  }

  public int getVal() {
    return val;
  }

  @Override
  public <T> T accept(NodeVisitor<T> visitor) {
    return visitor.visitIntegerNode(this);
  }

  @Override
  public boolean isImmediate() {
    return true;
  }

  @Override
  public int getCastedVal() {
    return getVal();
  }
}
