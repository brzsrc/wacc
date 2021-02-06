package Node.Expr;

public class StringNode extends ExprNode {

  private int length;

  public StringNode(String string) {
    this.value = string;
    this.length = string.length();
  }

  @Override
  public boolean check() {
      return false;
  }

  public int getLength() {
      return this.length;
  }
}
