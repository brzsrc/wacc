package frontend.node.expr;

import java.util.List;
import utils.NodeVisitor;
import utils.frontend.symbolTable.Symbol;

public class StructElemNode extends ExprNode {

  /* e.g. for a.b.c , offset would contain the offset of element b in the struct of a
   * and the offset of element c in the struct of a.b */
  private final List<Integer> offsets;
  private final String name;
  private final Symbol symbol;

  public StructElemNode(List<Integer> offsets, String name, Symbol symbol) {
    this.offsets = offsets;
    this.name = name;
    this.symbol = symbol;
  }

  public String getName() {
    return name;
  }

  public Symbol getSymbol() {
    return symbol;
  }

  public int getOffset(int index) {
    return offsets.get(index);
  }

  public int getDepth() {
    return offsets.size();
  }

  @Override
  public <T> T accept(NodeVisitor<T> visitor) {
    return visitor.visitStructElemNode(this);
  }
}
