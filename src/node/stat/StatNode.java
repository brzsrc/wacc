package node.stat;

import node.Node;

public abstract class StatNode implements Node {

  protected boolean leaveAtEnd;
  protected boolean hasReturn;


  /* Check current StatNode(statement) is SeqNode(sequential composition) or not */
  public boolean isSeq() {
    return false;
  }

  /* Setters */
  public void setLeaveAtEnd() { this.leaveAtEnd = false; }

  public void setHasReturn() { this.hasReturn = false; }

  /* All StatNode should call setAll at their constructors */
  protected void setAll() {
    setLeaveAtEnd();
    setHasReturn();
  }


  /* Getters */
  public boolean isLeaveAtEnd() {
    return leaveAtEnd;
  }

  public boolean isHasReturn() {
    return hasReturn;
  }
}
