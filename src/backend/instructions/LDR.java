package backend.instructions;

import utils.backend.Register

public class LDR extends Instruction {

  private Register regsiter;
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
