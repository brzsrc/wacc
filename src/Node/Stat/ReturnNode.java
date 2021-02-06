package utils.IR.CFG.stat;

import Node.StatNode;

public class ReturnNode implements StatNode {

  private

  @Override
  public boolean isReturn() {
    return true;
  }

  @Override
  public boolean hasEnd() {
    return true;
  }

}
