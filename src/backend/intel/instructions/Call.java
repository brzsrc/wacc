package backend.intel.instructions;

import backend.common.JmpInstruction;
import backend.intel.instructions.address.IntelAddress;

public class Call implements IntelInstruction {

  private String name;

  public Call(String name) {
    this.name = name;
  }

  @Override
  public String assemble() {
    return "call " + name;
  }
}
