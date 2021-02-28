package backend.instructions;

import utils.backend.Cond;

public class BL extends B {

  public BL(String label) {
    super(label, Bmode.BL);
  }

  public BL(Cond cond, String label) {
    super(cond, label, Bmode.BL);
  }

  public BL(String label, Bmode bmode) {
    super(label, bmode);
  }

  public BL(Cond cond, String label, Bmode bmode) {
    super(cond, label, bmode);
  }
}
