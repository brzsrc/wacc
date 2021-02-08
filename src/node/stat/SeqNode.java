package node.stat;

import java.util.ArrayList;
import java.util.List;

public class SeqNode extends StatNode {

  private final List<StatNode> stats = new ArrayList<>();

  public SeqNode(StatNode lhs, StatNode rhs) {
    add(lhs);
    add(rhs);
    setAll();
  }

  private void add(StatNode s) {
    if (s.isSeq()) {
      stats.addAll(((SeqNode) s).stats);
    } else {
      stats.add(s);
    }
  }

  @Override
  public boolean isSeq() {
    return true;
  }

  @Override
  public void setLeaveAtEnd() {
    assert stats.size() > 0;
    leaveAtEnd = stats.get(stats.size()-1).leaveAtEnd();
  }

  @Override
  public void setHasReturn() {
    for (StatNode node : stats) {
      if (node.hasReturn()) {
        hasReturn = true;
        return;
      }
    }
    hasReturn = false;
  }

}
