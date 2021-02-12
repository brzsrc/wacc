package node.stat;

import node.expr.ExprNode;
import type.Type;

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
  }
}
