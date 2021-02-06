package Node.Stat;

import java.util.ArrayList;
import java.util.List;

/**
 * describe one block of sequence-able instruction: assign/declare
 * or a single branch statement: function/scope
 * or a single special instruction: return/print/read/exit */
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
