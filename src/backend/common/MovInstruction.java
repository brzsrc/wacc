package backend.common;

import backend.common.address.Address;
import utils.backend.register.Register;

public abstract class MovInstruction {
  protected final Register Rd;
  protected final Address operand2;

  public MovInstruction(Register Rd, Address operand2) {
    this.Rd = Rd;
    this.operand2 = operand2;
  }
}
