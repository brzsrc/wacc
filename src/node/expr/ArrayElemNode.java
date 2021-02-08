package node.expr;

import node.Node;
import type.ArrayType;
import type.Type;
import utils.SymbolTable;

import java.util.List;

public class ArrayElemNode extends ExprNode {
  private ArrayNode array;
  private List<ExprNode> index;

  public ArrayElemNode(String value, ArrayNode array, List<ExprNode> index) {
    super(value);
    this.array = array;
    this.index = index;
  }

  @Override
  public Type getType(SymbolTable symbolTable) {
    // todo: support list index
    return ((ArrayType) array.type).getContentType();
  }

  @Override
  public void setType(Type type) {
    throw new UnsupportedOperationException("shouldn't call setType on arrayElemNode, type determined by array it belongs to");
  }
}