package Node.Stat;

public class ReturnNode implements StatNode {

  @Override
  public boolean isReturn() {
    return true;
  }

  @Override
  public boolean hasEnd() {
    return true;
  }

}
