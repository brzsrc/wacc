package node.stat;

import node.StatNode;

public class ExitNode implements StatNode {

  @Override
  public boolean hasEnd() {
    return true;
  }

}
