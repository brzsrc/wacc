package utils.IR.CFG.stat;

import Node.StatNode;

public class ExitNode implements StatNode {

  @Override
  public boolean hasEnd() {
    return true;
  }

}
