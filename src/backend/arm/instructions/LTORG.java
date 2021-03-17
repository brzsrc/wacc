package backend.arm.instructions;

public class LTORG implements ARMInstruction {

  @Override
  public String assemble() {
    return ".ltorg";
  }
}
