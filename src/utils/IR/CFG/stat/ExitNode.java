package utils.IR.CFG.stat;

import utils.IR.CFG.StatNode;

public class ExitNode implements StatNode {

  @Override
  public boolean hasEnd() {
    return true;
  }

}
