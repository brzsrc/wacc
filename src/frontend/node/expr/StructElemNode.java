package frontend.node.expr;

import java.util.List;
import utils.NodeVisitor;
import utils.frontend.symbolTable.Symbol;
import frontend.type.*;

public class StructElemNode extends ExprNode {

  /* e.g. for a.b.c , offset would contain the offset of element b in the struct of a
   * and the offset of element c in the struct of a.b */
  private final List<Integer> offsets;
  private final String name;
  private final Symbol symbol;
  /* for printAST only */
  private final List<String> elemNames;

  public StructElemNode(List<Integer> offsets, String name, Symbol symbol, List<String> elemNames, Type type) {
    this.offsets = offsets;
    this.name = name;
    this.symbol = symbol;
    this.elemNames = elemNames;
    this.type = type;
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

  public List<String> getElemNames() {
    return elemNames;
  }

  @Override
  public <T> T accept(NodeVisitor<T> visitor) {
    return visitor.visitStructElemNode(this);
  }
}
