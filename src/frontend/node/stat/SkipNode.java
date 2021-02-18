package frontend.node.stat;

import frontend.visitor.NodeVisitor;

public class SkipNode extends StatNode {

  @Override
  public void accept(NodeVisitor visitor) {
    visitor.visitSkipNode(this);
  }
  /**
   * Represent a SKIP statement
   */
}
