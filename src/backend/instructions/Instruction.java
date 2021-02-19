package backend.instructions;

public abstract class Instruction<T, K> {
  protected T fstExpr;
  protected K sndExpr;
  protected SudoRegister Rd;

  public Instruction() {
    fstExpr = null;
    sndExpr = null;
  }

  public void setFstExpr(T fstExpr) {
    this.fstExpr = fstExpr;
  }

  public void setSndExpr(K sndExpr) {
    this.sndExpr = sndExpr;
  }
}
