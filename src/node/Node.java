package node;

import node.expr.ArrayNode;
import node.expr.ExprNode;
import node.stat.StatNode;

public interface Node {

  /**
   * Base interface for ExprNode and StatNode Type casting is explicitly avoided by overriding the
   * following functions from a concrete class
   */

  default ExprNode asExprNode() {
    throw new IllegalArgumentException("cast not allowed");
  }

  default TypeDeclareNode asTypeDeclareNode() {
    throw new IllegalArgumentException("cast not allowed");
  }

  default StatNode asStatNode() {
    throw new IllegalArgumentException("cast not allowed");
  }

  default ArrayNode asArrayNode() {
    throw new IllegalArgumentException("cast not allowed");
  }
}
