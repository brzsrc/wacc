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
    // todo: instance of skip node does not add
    if (s instanceof ScopeNode) {
      body.addAll(((ScopeNode) s).body);
    } else {
      body.add(s);
    }
  }

  @Override
  protected void setLeaveAtEnd() {
    assert body.size() > 0;
    leaveAtEnd = body.get(body.size()-1).leaveAtEnd();
  }

  @Override
  protected void setHasReturn() {
    for (StatNode node : body) {
      if (node.hasReturn()) {
        hasReturn = true;
        return;
      }
    }
    hasReturn = false;
  }
}
