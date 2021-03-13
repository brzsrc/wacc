package backend.intel.instructions;

public class Leave implements IntelInstruction {

  @Override
  public String assemble() {
    return "leave";
  }
}
