package Node.Expr;

public class IntegerNode extends ExprNode<Integer> {

  public IntegerNode(Integer value) {
    this.value = value;
  }

  @Override
  public boolean check() {
    return false;
  }
}
