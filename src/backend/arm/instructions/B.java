package backend.arm.instructions;

import static utils.backend.Cond.NULL;

import utils.backend.Cond;

public class B extends ARMInstruction {

  /* B {cond} <label> */
  protected Label label;
  protected Cond cond;

  public B(String label) {
    this(NULL, label);
  }

  public B(Cond cond, String label) {
    this.cond = cond;
    this.label = new Label(label);
  }

  @Override
  public String assemble() {
    return "B" + cond + " " + label.getName();
  }
}
