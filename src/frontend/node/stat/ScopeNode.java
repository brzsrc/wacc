package frontend.node.stat;

import frontend.visitor.NodeVisitor;
import java.util.ArrayList;
import java.util.List;

public class ScopeNode extends StatNode {

  /**
   * Represent BEGIN <stat> END scope statement, as well as
   * <stat> ; <stat> sequential statement
   */

  private final List<StatNode> body = new ArrayList<>();

  public ScopeNode(StatNode node) {
    body.add(node);
    setLeaveAtEnd(getEndValue());
  }

  /* Handle the sequential statement */
  public ScopeNode(StatNode before, StatNode after) {
    mergeScope(before);
    mergeScope(after);
    setLeaveAtEnd(getEndValue());
  }

  private void mergeScope(StatNode s) {
    if (s instanceof ScopeNode) {
      body.addAll(((ScopeNode) s).body);
    } else if (!(s instanceof SkipNode)) {
      body.add(s);
    }
  }

  /* This will help to determine whether there is a return statement at the end of a sequence */
  private boolean getEndValue() {
    return !body.isEmpty() && body.get(body.size() - 1).leaveAtEnd();
  }

  public List<StatNode> getBody() {
    return body;
  }

  @Override
  public void accept(NodeVisitor visitor) {
    visitor.visitScopeNode(this);
  }
}
