package node.stat;

import node.ExprNode;
import node.StatNode;

public class IfNode implements StatNode {

  private final ExprNode cond;
  private final StatNode ifBody;
  private final StatNode elseBody;

  public IfNode(ExprNode cond, StatNode ifBody, StatNode elseBody) {
    this.cond = cond;
    this.ifBody = ifBody;
    this.elseBody = elseBody;
  }

  @Override
  public boolean hasEnd() {
    return ifBody.hasEnd() && elseBody.hasEnd();
  }
}
