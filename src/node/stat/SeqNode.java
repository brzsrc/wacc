package node.stat;

import java.util.ArrayList;
import java.util.List;
import node.StatNode;

public class SeqNode implements StatNode {

  private final List<StatNode> stats = new ArrayList<>();

  public void add(StatNode s) {
    if (s.isSeq()) {
      stats.addAll(((SeqNode) s).getStats());
    } else {
      stats.add(s);
    }
  }

  public List<StatNode> getStats() {
    return stats;
  }

  @Override
  public boolean isSeq() {
    return true;
  }

  @Override
  public boolean hasEnd() {
    assert stats.size() > 0;
    return stats.get(stats.size()-1).hasEnd();
  }

}
