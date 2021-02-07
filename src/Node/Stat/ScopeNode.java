package Node.Stat;


public class ScopeNode extends StatNode {

  private final StatNode body;

  public ScopeNode(StatNode body) {
    this.body = body;
    setAll();
  }

  @Override
  public void setLeaveAtEnd() {
    assert body != null;
    leaveAtEnd = body.isLeaveAtEnd();
  }

  @Override
  public void setHasReturn() {
    assert body != null;
    hasReturn = body.isHasReturn();
  }
}
