package node.stat;

import node.Node;
import utils.SymbolTable;

public abstract class StatNode implements Node {

  protected boolean leaveAtEnd;
  protected boolean hasReturn;
  /* Statement scope, SeqNode and ScopeNode actually do not need */
  private SymbolTable scope;

  /*
   * Check current StatNode(statement) is SeqNode(sequential composition) or not
   */
  public boolean isSeq() {
    return false;
  }

  /* Setters */
  public void setLeaveAtEnd() {
    this.leaveAtEnd = false;
  }

  public void setHasReturn() {
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
}
