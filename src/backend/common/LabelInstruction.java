package backend.common;

import backend.Instruction;
import backend.arm.instructions.Label;

public abstract class LabelInstruction implements Instruction {

  protected final String labelName;

  public LabelInstruction(String labelName) {
    this.labelName = labelName;
  }

  public Label asArmLabel() {
    return (Label) this;
  }

  public backend.intel.instructions.Label asIntelLabel() {
    return (backend.intel.instructions.Label) this;
  }

  public String getName() {
    return labelName;
  }

  @Override
  public String assemble() {
    return labelName + ":";
  }
}
