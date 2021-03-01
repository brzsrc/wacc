package frontend.node.expr;

import frontend.type.Type;

import utils.NodeVisitor;
import utils.frontend.symbolTable.Symbol;

import java.util.List;

import static utils.frontend.SemanticErrorHandler.arrayDepthError;

public class ArrayElemNode extends ExprNode {

  /**
   * Represent the array_elem expression
   * Examples: a[0], a[2][7], b[5], where `a` and `b` are arrays
   */

  /* the string identifier of the array */
  private final String ident;
  /* the array where this array_elem is located */
  private final ExprNode array;
  /* a list of indices needed in multilevel indexing. e.g. a[3][4][5] */
  private final List<ExprNode> index;
  /* record the arrayDepth and indexDepth to decide if the indexing has too many layers */
  private final int arrayDepth, indexDepth;

  private final Symbol symbol;

  public ArrayElemNode(ExprNode array, List<ExprNode> index, Type type, String ident, Symbol symbol) {
    this.array = array;
    this.index = index;
    this.type = type;
    this.arrayDepth = array.getType().asArrayType().getDepth();
    this.indexDepth = index.size();
    this.ident = ident;
    this.weight = 1;
    this.symbol = symbol;

    if (arrayDepth < indexDepth) {
      arrayDepthError(null, array.getType(), index.size());
    }
  }

  public Symbol getSymbol() {
    return symbol;
  }

  public String getName() {
    return ident;
  }

  public ExprNode getArray() {
    return array;
  }

  public List<ExprNode> getIndex() {
    return index;
  }

  public int getDepth() {
    return indexDepth;
  }

  @Override
  public <T> T accept(NodeVisitor<T> visitor) {
    return visitor.visitArrayElemNode(this);
  }

}