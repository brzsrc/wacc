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
  /* for printAST only */
  private final String name;

  public StructDeclareNode(List<IdentNode> elements, String name) {
    this.elements = elements;
    this.name = name;
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

  public IdentNode getElem(int index) {
    return elements.get(index);
  }

  public int getElemCount() {
    return elements.size();
  }

  public List<Integer> getOffsets() {
    return offsets;
  }

  public int getSize() {
    return size;
  }

  public String getName() {
    return name;
  }

  @Override
  public <T> T accept(NodeVisitor<T> visitor) {
    return null;
  }
}
