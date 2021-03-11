package backend.intel.instructions;

import backend.common.LabelInstruction;

public class Label extends LabelInstruction implements IntelInstruction {

  public Label(String labelName) {
    super(labelName);
  }
}
