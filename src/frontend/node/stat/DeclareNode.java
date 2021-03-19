package frontend.node.stat;

import frontend.node.expr.ExprNode;
import frontend.type.Type;
import utils.NodeVisitor;

public class DeclareNode extends StatNode {

  /**
   * Represent a declaration statement, with identifier and rhs recorded
   * Example: int x = 5, char c = '!', bool b = false
   */

  private final String identifier;
  private final ExprNode rhs;

  public DeclareNode(String identifier, ExprNode rhs) {
    this.identifier = identifier;
    this.rhs = rhs;
    this.minStackSpace = rhs.getType().getSize();
  }

  public String getIdentifier() {
    return identifier;
  }

  public ExprNode getRhs() {
    return rhs;
  }

  @Override
  public <T> T accept(NodeVisitor<T> visitor) {
    return visitor.visitDeclareNode(this);
  }
}
