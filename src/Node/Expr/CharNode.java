package Node.Expr;

public class CharNode extends ExprNode {

  public CharNode(String c) {
    this.value = c;
  }

  public int getAsciiValue() {
    return value.charAt(0);
  }

  @Override
  public boolean check() {
    return false;
  }
}
