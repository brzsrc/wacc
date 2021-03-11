package backend.arm.instructions;

import static utils.backend.Cond.NULL;

import backend.common.JmpInstruction;
import utils.backend.Cond;

public class B extends JmpInstruction implements ARMInstruction {

  /* B {cond} <label> */

  public B(String label) {
    super(label);
  }

  public B(Cond cond, String label) {
    super(cond, label);
  }

  @Override
  public String assemble() {
    return "B" + cond + " " + label.getName();
  }
}
