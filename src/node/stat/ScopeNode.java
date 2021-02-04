package node.stat;

import node.StatNode;

public class ScopeNode implements StatNode {

  private final StatNode body;

  public ScopeNode(StatNode body) {
    this.body = body;
  }

  @Override
  public boolean hasEnd() {
    return body.hasEnd();
  }
}
