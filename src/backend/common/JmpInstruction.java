package backend.common;

import static utils.backend.Cond.NULL;

import backend.Instruction;
import backend.arm.instructions.Label;
import utils.backend.Cond;

public abstract class JmpInstruction implements Instruction {
  protected Label label;
  protected Cond cond;

  public JmpInstruction(String label) {
    this(NULL, label);
  }

  public JmpInstruction(Cond cond, String label) {
    this.cond = cond;
    this.label = new Label(label);
  }
}
