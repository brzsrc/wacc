package Node.Stat;

import Node.Expr.ExprNode;
import utils.SymbolTable;

public class WhileNode implements StatNode {

  private final ExprNode cond;
  private final ScopeNode body;
  private SymbolTable bodySymbolTable;

  public WhileNode(ExprNode cond, ScopeNode body) {
    this.cond = cond;
    this.body = body;
  }

  public SymbolTable getSymbolTable() {
    return this.bodySymbolTable;
  }
}
