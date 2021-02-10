package node;

import node.expr.ArrayElemNode;
import node.expr.ExprNode;
import node.expr.PairNode;
import node.stat.StatNode;
import type.PairType;

public interface Node {

  default ExprNode asExprNode() {
    throw new IllegalArgumentException("cast not allowed");
  }

  default TypeDeclareNode asTypeDeclareNode() {
    throw new IllegalArgumentException("cast not allowed");
  }

  default StatNode asStatNode() {
    throw new IllegalArgumentException("cast not allowed");
  }

}
