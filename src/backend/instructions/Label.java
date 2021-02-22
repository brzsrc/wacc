package backend.instructions;

public class Label extends Instruction {
  private final int labelNum;

  public Label(int labelNum) {
    this.labelNum = labelNum;
  }

  @Override
  public String assemble() {
    return toString() + ":";
  }

  @Override
  public String toString() {
    return "Label" + labelNum;
  }
}
