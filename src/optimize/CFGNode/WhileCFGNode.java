package optimize.CFGNode;

import frontend.node.expr.ExprNode;
import frontend.node.stat.WhileNode;

public class WhileCFGNode {

  private ExprNode cond;

  public WhileCFGNode(ExprNode cond) {
    super();
    this.cond = cond;
  }
}
