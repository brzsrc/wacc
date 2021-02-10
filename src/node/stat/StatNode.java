package node.stat;

import node.Node;
import utils.SymbolTable;

public abstract class StatNode implements Node {

  protected boolean leaveAtEnd;
  protected boolean hasReturn;
  /* Statement scope, ScopeNode actually do not need */
  protected SymbolTable scope;

  /* Setters, protected to prevent misuse from outside*/
  protected void setLeaveAtEnd() {
    this.leaveAtEnd = false;
  }

  protected void setHasReturn() {
    this.hasReturn = false;
  }

  /* All StatNode should call setAll at their constructors */
  protected void setAll() {
    setLeaveAtEnd();
    setHasReturn();
  }

  public void setScope(SymbolTable scope) {
    this.scope = scope;
  }

  /* Getters */
  public boolean leaveAtEnd() {
    return leaveAtEnd;
  }

  public boolean hasReturn() {
    return hasReturn;
  }

  @Override
  public StatNode asStatNode() {
    return this;
  }
}
