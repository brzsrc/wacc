package backend.arm.instructions;

import backend.common.address.Address;
import utils.backend.register.Register;

public class STR implements ARMInstruction {

  private final Register srcReg;
  private final Address addr;
  private final StrMode mode;

  public STR(Register srcReg, Address addr, StrMode mode) {
    this.srcReg = srcReg;
    this.addr = addr;
    this.mode = mode;
  }

  public STR(Register srcReg, Address addr) {
    this(srcReg, addr, StrMode.STR);
  }

  @Override
  public String assemble() {
    return mode.name() + " " + srcReg + ", " + addr;
  }

  /* STR{cond} <Rd>, <a_mode2> */
  public enum StrMode {STR, STRB}
}
