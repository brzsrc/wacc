package backend.common;

import backend.Instruction;
import backend.common.address.Address;
import utils.backend.register.Register;

public abstract class CmpInstruction implements Instruction {

  /* represent the genetic compare instruction */

  protected final Register Rd;
  protected final Address operand2;

  public CmpInstruction(Register Rd, Address operand2) {
    this.Rd = Rd;
    this.operand2 = operand2;
  }
}
