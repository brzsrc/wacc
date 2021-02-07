package node.expr;

public class ArrayElemNode extends ExprNode {
  private ArrayNode array;
  private IntegerNode index;


  @Override
  public boolean check() {
    return false;
  }
}