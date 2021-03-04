package backend.instructions;

public class LTORG extends Instruction{
  @Override
  public String assemble() {
    return ".ltorg";
  }
}
