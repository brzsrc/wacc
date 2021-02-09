package node.stat;

import java.util.ArrayList;
import java.util.List;

public class ScopeNode extends StatNode {

  private final List<StatNode> body = new ArrayList<>();

  public ScopeNode(StatNode node) {
    mergeScope(node);
    setAll();
  }

  /* Handle the SeqStat */
  public ScopeNode(StatNode before, StatNode after) {
    mergeScope(before);
    mergeScope(after);
    setAll();
  }

  private void mergeScope(StatNode s) {
    if (s instanceof ScopeNode) {
      body.addAll(((ScopeNode) s).body);
    } else {
      body.add(s);
    }
  }

  @Override
  public void setLeaveAtEnd() {
    assert body.size() > 0;
    leaveAtEnd = body.get(body.size()-1).leaveAtEnd();
  }

  @Override
  public void setHasReturn() {
    for (StatNode node : body) {
      if (node.hasReturn()) {
        hasReturn = true;
        return;
      }
    }
    hasReturn = false;
  }
}
