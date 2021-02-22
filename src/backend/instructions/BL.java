package backend.instructions;

public class BL extends Instruction {
  private String label;

  public BL(String label) {
    this.label = label;
  }

  @Override
  public String assemble() {
    return "BL " + label;
  }
}
