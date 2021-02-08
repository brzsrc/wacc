package node.stat;

public class ScopeNode extends StatNode {

  private final StatNode body;

  public ScopeNode(StatNode body) {
    this.body = body;
    setAll();
  }

  @Override
  public void setLeaveAtEnd() {
    assert body != null;
    leaveAtEnd = body.leaveAtEnd();
  }

  @Override
  public void setHasReturn() {
    assert body != null;
    hasReturn = body.hasReturn();
  }
}
