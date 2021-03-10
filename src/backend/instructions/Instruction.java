package backend.instructions;

public abstract class Instruction {

  public abstract String assemble();

  public int getIndentationLevel() {
    return 2;
  }
}
