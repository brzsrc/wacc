package backend.common;

import backend.common.address.Address;
import utils.backend.register.Register;

public abstract class MovInstruction {

  /**
   * In Intel x86-64 instruction, the register field here will be used as the destination register
   */

  protected final Register Rs;
  protected final Register Rd;
  protected final Address operand2;

  public MovInstruction(Register rs, Register rd, Address operand2) {
    Rs = rs;
    Rd = rd;
    this.operand2 = operand2;
  }

  /* represent the case of moving operand2 to Rd */
  public MovInstruction(Register register, Address operand2) {
    this(null, register, operand2);
  }

  /* represent the case of moving Rs to Rd */
  public MovInstruction(Register Rs, Register Rd) {
    this(Rs, Rd, null);
  }

  /* represent the case of moving Rs to operand2 */
  public MovInstruction(Address operand2, Register Rs) {
    this(Rs, null, operand2);
  }
}
