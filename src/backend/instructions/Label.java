package backend.instructions;

public class Label extends Instruction {
  /* for example: msg_9: / f_createNewNode: / L0: */
  private String labelName;

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
