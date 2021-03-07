package frontend.node;

import frontend.node.expr.IdentNode;
import frontend.type.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utils.NodeVisitor;

public class StructDeclareNode implements Node {

  /* use array cause the length should not change */
  private final List<IdentNode> elements;
  private final List<Integer> offsets;
  private final Map<String, IdentNode> elemNameMap;
  private final Map<String, Integer> offsetMap;
  private final int size;

  public StructDeclareNode(List<IdentNode> elements) {
    this.elements = elements;
    offsets = new ArrayList<>();
    elemNameMap = new HashMap<>();
    offsetMap = new HashMap<>();
    int offset = 0;
    for (IdentNode i : elements) {
      offsets.add(offset);
      elemNameMap.put(i.getName(), i);
      offsetMap.put(i.getName(), offset);
      offset += i.getType().getSize();
    }
    size = offset;
  }

  public IdentNode findElem(String name) {
    return elemNameMap.get(name);
  }

  public int findOffset(String name) {
    return offsetMap.get(name);
  }

  public Type getElemType(int index) {
    return elements.get(index).getType();
  }

  public List<Integer> getOffsets() {
    return offsets;
  }

  public int getSize() {
    return size;
  }

  @Override
  public <T> T accept(NodeVisitor<T> visitor) {
    return null;
  }
}
