package node.stat;

import java.util.ArrayList;
import java.util.List;

public class ScopeNode extends StatNode {

  private final List<StatNode> body = new ArrayList<>();

  public ScopeNode(StatNode node) {
    mergeScope(node);
    setLeaveAtEnd(getEndValue());
  }

  /* Handle the SeqStat */
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


  private boolean getEndValue() {
    return !body.isEmpty() && body.get(body.size() - 1).leaveAtEnd();
  }

}
