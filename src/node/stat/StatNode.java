package node.stat;

import node.Node;
import utils.SymbolTable;

public abstract class StatNode implements Node {

  protected boolean leaveAtEnd = false;
  protected SymbolTable scope;

  /* Set leaveAtEnd if needs overwrite */
  protected void setLeaveAtEnd() {
    this.leaveAtEnd = false;
  }

  public void setScope(SymbolTable scope) {
    this.scope = scope;
  }

  /* Getters */
  public boolean leaveAtEnd() {
    return leaveAtEnd;
  }

  @Override
  public StatNode asStatNode() {
    return this;
  }
}
