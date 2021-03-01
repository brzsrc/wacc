package frontend.node.expr;

import frontend.type.Type;
import utils.NodeVisitor;
import utils.frontend.symbolTable.Symbol;

public class IdentNode extends ExprNode {

  /**
   * Represent an identifier node, including its type and identifier
   * Example: int x, char c, int[] arr
   */

  private final String name;

  private Symbol symbol;

  public IdentNode(Type type, String name) {
    this.type = type;
    this.name = name;
    this.weight = 1;
  }

  public void setSymbol(Symbol symbol) {
    this.symbol = symbol;
  }

  public Symbol getSymbol() {
    return symbol;
  }

  public String getName() {
    return name;
  }

  @Override
  public <T> T accept(NodeVisitor<T> visitor) {
    return visitor.visitIdentNode(this);
  }
}
