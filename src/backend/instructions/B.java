package backend.instructions;

import utils.backend.Cond;

public class B extends Instruction {
  protected String label;
  protected Cond cond;

  public B(String label) {
    this(Cond.NULL, label);
  }

  public B(Cond cond, String label) {
    this.cond = cond;
    this.label = label;
  }

  @Override
  public String assemble() {
    return "B " + label;
  }
}
