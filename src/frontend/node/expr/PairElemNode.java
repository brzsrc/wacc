package frontend.node.expr;

import frontend.type.Type;

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

  @Override
  public void showNode(int leadingSpace) {
    System.out.print(isFist ? "fst " : "snd ");
    pair.showNode(0);
  }
}
