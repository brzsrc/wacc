package frontend.node.expr;

import frontend.type.Type;

import frontend.visitor.NodeVisitor;
import java.util.ArrayList;
import java.util.List;

import static frontend.utils.SemanticErrorHandler.arrayDepthError;

public class ArrayElemNode extends ExprNode {

  /**
   * Represent the array_elem expression
   * Examples: a[0], a[2][7], b[5], where `a` and `b` are arrays
   */

  /* the array where this array_elem is located */
  private final ExprNode array;
  /* a list of indices needed in multilevel indexing. e.g. a[3][4][5] */
  private final List<ExprNode> index;

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
      arrayDepthError(null, array.getType(), index.size());
    }
  }

  public ExprNode getArray() {
    return array;
  }

  public List<ExprNode> getIndex() {
    return index;
  }

  @Override
  public void accept(NodeVisitor visitor) {
    visitor.visitArrayElemNode(this);
  }
}