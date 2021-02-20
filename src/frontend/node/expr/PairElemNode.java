package frontend.node.expr;

import frontend.type.Type;
import frontend.visitor.NodeVisitor;

public class PairElemNode extends ExprNode {

  /**
   * Represent a pair_elem node, with the <expr> and fst/snd recorded
   * Example: fst <expr>, snd <expr>
   */

  private final ExprNode pair;
  private final boolean isFist;

  public PairElemNode(ExprNode pair, Type type, boolean isFirst) {
    this.pair = pair;
    this.type = type;
    this.isFist = isFirst;
  }

  public boolean isFist() {
    return isFist;
  }

  public ExprNode getPair() {
    return pair;
  }

  @Override
  public <T> T accept(NodeVisitor<T> visitor) {
    return visitor.visitPairElemNode(this);
  }
}
