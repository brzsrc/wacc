package backend.instructions;

import backend.instructions.addressing.Addressing;
import utils.backend.Register;

public class STR extends Instruction {

  private Register srcReg;
  private Addressing addr;

  public STR(Register srcReg, Addressing addr) {
    this.srcReg = srcReg;
    this.addr = addr;
  }


  @Override
  public String assemble() {
    return null;
  }
}
