package node.expr;

import type.ArrayType;
import type.Type;
import utils.SymbolTable;

public class ArrayElemNode extends ExprNode {
  private ArrayNode array;
  private ExprNode index;

  public ArrayElemNode(String value, ArrayNode array, ExprNode index) {
    super(value);
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