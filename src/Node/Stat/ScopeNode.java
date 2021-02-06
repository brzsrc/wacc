package Node.Stat;

import java.util.List;

public class ScopeNode implements StatNode {

  private final List<SeqNode> body;

  public ScopeNode(List<SeqNode> body) {
    this.body = body;
  }

  @Override
  public boolean hasEnd() {
    // return body.hasEnd();
    return false;
  }
}
