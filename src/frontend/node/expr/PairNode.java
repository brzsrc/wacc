package frontend.node.expr;

import frontend.type.PairType;
import frontend.visitor.NodeVisitor;

public class PairNode extends ExprNode {

  /**
   * Represent a pair. Notice that subpair type coercing will be handler in type system as follows:
   *  - The concrete types of subpairs will be stored in the node system.
   *  - However, the subpair types will be set to null in the type system in order to implement type coercing.
   *  - e.g. pair(int, pair(char, bool)) will have type Pair<INTEGER, Pair<null, null>>, but the CharNode
   *    and BoolNode will still be stored as ExprNode in fst and snd of the sub-PairNode.
   *  - In the type system, a null pair will be represented as `Pair<null, null>`, whereas a pair with
   *    two subpairs will be represented as `Pair<Pair<null, null>, Pair<null, null>>`
   *
   * Example: newpair(5, 'a'), null
   */

  private ExprNode fst;
  private ExprNode snd;

  public PairNode(ExprNode fst, ExprNode snd) {
    this.fst = fst;
    this.snd = snd;
    this.type = new PairType(fst.type, snd.type);
  }

  public PairNode() {
    this.fst = null;
    this.snd = null;
    this.type = new PairType();
  }

  public ExprNode getFst() {
    return fst;
  }

  public ExprNode getSnd() {
    return snd;
  }

  public void setFst(ExprNode fst) {
    this.fst = fst;
  }

  public void setSnd(ExprNode snd) {
    this.snd = snd;
  }

  @Override
  public void accept(NodeVisitor visitor) {
    visitor.visitPairNode(this);
  }

}
