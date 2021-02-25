package backend.instructions;

import backend.instructions.addressing.Addressing;
import utils.backend.Register;

public class LDR extends Instruction {

  private Register register;
  private Addressing addr;

  public LDR(Register register, Addressing addr) {
    this.register = register;
    this.addr = addr;
  }

  @Override
  public String assemble() {
    return null;
  }
}
