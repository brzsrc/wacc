package backend.arm.instructions;

import backend.common.LabelInstruction;

public class Label extends LabelInstruction implements ARMInstruction {

  public Label(String labelName) {
    super(labelName);
  }

  /* for example: msg_9: / f_createNewNode: / L0: */

  @Override
  public String assemble() {
    return labelName + ":";
  }

  public String getName() {
    return labelName;
  }

  @Override
  public int getIndentationLevel() {
    return 1;
  }
}
