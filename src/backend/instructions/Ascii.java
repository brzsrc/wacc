package backend.instructions;

public class Ascii extends Instruction {

  private final String value;

  public Ascii(String value) {
    this.value = value;
  }

  @Override
  public String assemble() {
    return null;
  }
}
