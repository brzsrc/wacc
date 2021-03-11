package backend.common;

import backend.Instruction;

public abstract class LabelInstruction implements Instruction {

  protected final String labelName;

  public LabelInstruction(String labelName) {
    this.labelName = labelName;
  }

  @Override
  public String assemble() {
    return null;
  }
}
