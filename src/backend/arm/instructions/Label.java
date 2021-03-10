package backend.arm.instructions;

public class Label extends ARMInstruction {

  /* for example: msg_9: / f_createNewNode: / L0: */
  private final String labelName;

  public Label(String labelName) {
    this.labelName = labelName;
  }

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
