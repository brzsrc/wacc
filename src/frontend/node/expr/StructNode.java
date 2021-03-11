package frontend.node.expr;

import frontend.type.StructType;
import java.util.List;
import utils.NodeVisitor;
import utils.Utils;

public class StructNode extends ExprNode {

  private List<ExprNode> elemValues;
  private List<Integer> elemOffsets;
  private int size;
  /* for printAST only */
  private String name;
  /* if the struct variable is not = empty, then it is initialised */
  private final boolean isInitialised;

  public StructNode(List<ExprNode> elemValues, List<Integer> elemOffsets, int size, String name) {
    this.elemValues = elemValues;
    this.elemOffsets = elemOffsets;
    this.size = size;
    this.name = name;
    isInitialised = true;
    type = new StructType(name);
  }

  public StructNode() {
    isInitialised = false;
    type = Utils.STRUCT_TYPE;
  }

  public boolean isInitialised() {
    return isInitialised;
  }

  public int getSize() {
    return size;
  }

  public String getName() {
    return name;
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
