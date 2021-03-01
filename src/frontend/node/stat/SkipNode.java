package frontend.node.stat;

import utils.NodeVisitor;
import utils.frontend.symbolTable.SymbolTable;

public class SkipNode extends StatNode {

  public SkipNode(SymbolTable scope) {
    setScope(scope);
  }

  @Override
  public <T> T accept(NodeVisitor<T> visitor) {
    return visitor.visitSkipNode(this);
  }
  /**
   * Represent a SKIP statement
   */
}
