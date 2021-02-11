package node.stat;

import node.Node;
import utils.SymbolTable;

public abstract class StatNode implements Node {

  private boolean leaveAtEnd = false;
  private SymbolTable scope;

  /* Set leaveAtEnd if needs overwrite */
  protected void setLeaveAtEnd(boolean value) {
    this.leaveAtEnd = value;
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
