package node.expr;

import node.Node;
import type.ArrayType;
import type.Type;
import utils.SemanticErrorHandler;
import utils.SymbolTable;

import java.util.ArrayList;
import java.util.List;

public class ArrayElemNode extends ExprNode {
  private ExprNode array;
  private List<ExprNode> index;

  public ArrayElemNode(ExprNode array, ExprNode index, Type type) {
    this.array = array;
    this.index = new ArrayList<>();
    this.index.add(index);
    this.type = type;
  }

  public ArrayElemNode(ExprNode array, List<ExprNode> index, Type type) {
    this.array = array;
    this.index = index;
    this.type = type;

    if (array.getType().asArrayType().getDepth() < index.size()) {
      SemanticErrorHandler.arrayDepthError(null, array.getType(), index.size());
    }
  }
}