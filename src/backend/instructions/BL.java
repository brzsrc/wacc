package backend.instructions;

import utils.backend.Cond;

public class BL extends B {

  /* BL {cond} <label> */
  public BL(String label) {
    super(label);
  }

  public BL(Cond cond, String label) {
    super(cond, label);
  }

  @Override
  public String assemble() {
    return "BL" + cond + " " + label.getName();
  }

}
