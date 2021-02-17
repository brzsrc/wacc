package frontend.node.expr;

import frontend.type.Type;

public class IdentNode extends ExprNode {

  /**
   * Represent an identifier node, including its type and identifier
   * Example: int x, char c, int[] arr
   */

  private final String name;

  public IdentNode(Type type, String name) {
    this.type = type;
    this.name = name;
  }

  public String getName() {
    return name;
  }

}
