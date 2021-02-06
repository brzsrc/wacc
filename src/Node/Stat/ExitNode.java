package Node.Stat;

public class ExitNode implements StatNode {

  @Override
  public boolean hasEnd() {
    return true;
  }

  public boolean isSeq() {
    return true;
  }

  public boolean isReturn() {
    return false;
  }

}
