package backend.instructions;

public class Word extends Instruction {

  private final int length;

  public Word(int length) {
    this.length = length;
  }

  @Override
  public String assemble() {
    return null;
  }
}
