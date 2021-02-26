package backend.instructions;

import backend.instructions.addressing.Addressing;
import utils.backend.Register;

public class LDR extends Instruction {

  private Register register;
  private Addressing addr;
  /* 0: LDR, 1: LDREQ, -1: LDRNE */
  private int cond = 0;

  public LDR(Register register, Addressing addr) {
    this.register = register;
    this.addr = addr;
  }

  public LDR(Register register, Addressing addr, boolean isEq) {
    this.register = register;
    this.addr = addr;
    cond = (isEq)? cond + 1 : cond - 1;
  }


  @Override
  public String assemble() {
    return null;
  }
}
