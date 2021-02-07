package Node.Stat;

import Node.Expr.ExprNode;
import utils.SymbolTable;

public class WhileNode implements StatNode {

  private final ExprNode cond;
  private final ScopeNode body;
  private SymbolTable bodySymbolTable;

  public WhileNode(ExprNode cond, ScopeNode body, SymbolTable bodySymbolTable) {
    this.cond = cond;
    this.body = body;
    this.bodySymbolTable = bodySymbolTable;
  }

  public SymbolTable getSymbolTable() {
    return this.bodySymbolTable;
  }

  @Override
  public boolean hasReturn() {
    return body.hasReturn();
  }
}
