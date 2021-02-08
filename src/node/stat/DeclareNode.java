package node.stat;

import node.expr.ExprNode;
import type.Type;

public class DeclareNode extends StatNode {

  private final Type type;
  private final ExprNode identifier;
  private final ExprNode rhs;

  public DeclareNode(Type type, ExprNode identifier, ExprNode rhs) {
    this.type = type;
    this.identifier = identifier;
    this.rhs = rhs;
  }
}
