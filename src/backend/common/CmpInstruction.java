package backend.common;

import backend.Instruction;
import backend.common.address.Address;
import utils.backend.register.Register;

public abstract class CmpInstruction implements Instruction {

  /* represent the genetic compare instruction */

  protected final Register Rd;
  protected final Register Rs;
  protected final Address operand2;

  private CmpInstruction(Register rd, Register rs, Address operand2) {
    Rd = rd;
    Rs = rs;
    this.operand2 = operand2;
  }

  public CmpInstruction(Register rd, Address operand2) {
    this(rd, null, operand2);
  }

  public CmpInstruction(Register rd, Register rs) {
    this(rd, rd, null);
  }
}
