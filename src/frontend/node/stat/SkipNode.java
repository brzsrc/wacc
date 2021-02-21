package frontend.node.stat;

import utils.NodeVisitor;

public class SkipNode extends StatNode {

  @Override
  public <T> T accept(NodeVisitor<T> visitor) {
    return visitor.visitSkipNode(this);
  }
  /**
   * Represent a SKIP statement
   */
}
