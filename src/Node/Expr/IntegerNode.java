package Node.Expr;

public class IntegerNode extends ExprNode {

  public IntegerNode(String value) {
    this.value = value;
  }

  @Override
  public boolean check() {
    return false;
  }
}
