package backend.directives;

import backend.instructions.Instruction;

public class Label extends Instruction {
  
  private String labelName;

  public Label(String labelName) {
    this.labelName = labelName;
  }

  @Override
  public String assemble() {
    return labelName + ":";
  }
}
