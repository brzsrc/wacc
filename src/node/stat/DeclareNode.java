package node.stat;

import node.expr.ExprNode;
import type.Type;

public class DeclareNode extends StatNode {

  private final String identifier;
  private final ExprNode rhs;

  public DeclareNode(String identifier, ExprNode rhs) {
    this.identifier = identifier;
    this.rhs = rhs;
  }
}
