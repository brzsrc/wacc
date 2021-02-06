package Node.Expr;

public class CharNode extends ExprNode<Character> {

  public CharNode(char c) {
      this.value = c;
  }

  public int getAsciiValue() {
    return this.value;
  }

  @Override
  public boolean check() {
      return false;
  }
}
