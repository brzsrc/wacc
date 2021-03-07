package frontend.node.expr;

import java.util.List;
import utils.NodeVisitor;

public class StructNode extends ExprNode {

  private List<ExprNode> elemValues;
  private List<Integer> elemOffsets;
  private int size;
  /* for a struct which contains other struct, then it could be initialised as {a, b, null, c}
   * the null means the inner struct element which is not initialised here */
  private final boolean isInitialised;

  public StructNode(List<ExprNode> elemValues, List<Integer> elemOffsets, int size) {
    this.elemValues = elemValues;
    this.elemOffsets = elemOffsets;
    this.size = size;
    isInitialised = true;
  }

  public StructNode() {
    isInitialised = false;
  }

  public boolean isInitialised() {
    return isInitialised;
  }

  public int getSize() {
    return size;
  }

  public int getElemCount() {
    return elemValues.size();
  }

  public ExprNode getElem(int index) {
    return elemValues.get(index);
  }

  public int getElemOffset(int index) {
    return elemOffsets.get(index);
  }

  @Override
  public <T> T accept(NodeVisitor<T> visitor) {
    return visitor.visitStructNode(this);
  }
}
