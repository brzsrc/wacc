package backend.intel.instructions;

public class Cltd implements IntelInstruction {

  @Override
  public String assemble() {
    return "cltd";
  }
}
