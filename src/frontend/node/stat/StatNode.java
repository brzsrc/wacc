package frontend.node.stat;

import frontend.node.Node;
import frontend.utils.SymbolTable;

public abstract class StatNode implements Node {

  /**
   * abstract class for statement ndoes. Statement nodes do not have type
   */

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

  protected void appendLeadingSpace(int leadingSpace) {
    for (int i = 0; i < leadingSpace; i++) {
      System.out.print(" ");
    }
  }
}
