package backend.arm.instructions;

public class LTORG extends ARMInstruction {

  @Override
  public String assemble() {
    return ".ltorg";
  }
}
