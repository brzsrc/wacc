package node.expr;

import node.Node;
import type.ArrayType;
import type.Type;
import utils.SymbolTable;

import java.util.ArrayList;
import java.util.List;

public class ArrayElemNode extends ExprNode {
  private ArrayNode array;
  private List<ExprNode> index;

  public ArrayElemNode(ArrayNode array, ExprNode index) {
    this.array = array;
    this.index = new ArrayList<>();
    this.index.add(index);
  }

  public ArrayElemNode(ArrayNode array, List<ExprNode> index) {
    this.array = array;
    this.index = index;
  }

  @Override
  public Type getType(SymbolTable symbolTable) {
    return ((ArrayType) array.type).getContentType();
  }

  @Override
  public void setType(Type type) {
    throw new UnsupportedOperationException("shouldn't call setType on arrayElemNode, type determined by array it belongs to");
  }
}