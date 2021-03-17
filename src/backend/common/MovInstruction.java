package backend.common;

import backend.common.address.Address;
import utils.backend.register.Register;

public abstract class MovInstruction {

  /**
   * In Intel x86-64 instruction, the register field here will be used as the destination register
   */

  protected final Register rs;
  protected final Register rd;
  protected final Address operand2;

  public MovInstruction(Register rs, Register rd, Address operand2) {
    this.rs = rs;
    this.rd = rd;
    this.operand2 = operand2;
  }

  /* represent the case of moving operand2 to rd */
  public MovInstruction(Register register, Address operand2) {
    this(null, register, operand2);
  }

  /* represent the case of moving rs to rd */
  public MovInstruction(Register rs, Register rd) {
    this(rs, rd, null);
  }

  /* represent the case of moving rs to operand2 */
  public MovInstruction(Address operand2, Register rs) {
    this(rs, null, operand2);
  }
}
