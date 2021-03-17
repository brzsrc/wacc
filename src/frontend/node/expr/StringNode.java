package frontend.node.expr;

import frontend.type.BasicType;
import frontend.type.BasicTypeEnum;
import utils.NodeVisitor;
import utils.Utils.AssemblyArchitecture;

public class StringNode extends ExprNode {

  /**
   * Represent a string
   * Example: "hello, world!"
   */

  private final String string;

  public StringNode(String string, AssemblyArchitecture arch) {
    this.string = string;
    this.type = new BasicType(BasicTypeEnum.STRING, arch);
    this.weight = 1;
  }

  public int getLength() {
    return this.string.length();
  }

  public String getString() {
    return string;
  }

  @Override
  public <T> T accept(NodeVisitor<T> visitor) {
    return visitor.visitStringNode(this);
  }
}
