package node.expr;

public class FunctionCallNode extends ExprNode {

  public FunctionCallNode(String c) {
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
