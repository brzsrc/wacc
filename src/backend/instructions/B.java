package backend.instructions;

import utils.backend.Cond;

public class B extends Instruction {

  public enum Bmode {
    B, BL, BLEQ, BLLT, BLCS
  }

  protected Label label;
  protected Cond cond;
  protected Bmode bMode;

  public B(String label) {
    this(Cond.NULL, label);
  }

  public B(String label, Bmode bl) {
    this(Cond.NULL, label, bl);
  }

  public B(Cond cond, String label) {
    this(cond, label, Bmode.B);
  }

  public B(Cond cond, String label, Bmode bMode) {
    this.cond = cond;
    this.label = new Label(label);
    this.bMode = bMode;
  }

  @Override
  public String assemble() {
    return bMode.name() + " " + label.getName();
  }
}
