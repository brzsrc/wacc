package backend.arm.instructions;

import backend.arm.instructions.addressing.Addressing;
import utils.backend.register.Register;

public class STR extends ARMInstruction {

  private final Register srcReg;
  private final Addressing addr;
  private final StrMode mode;

  public STR(Register srcReg, Addressing addr, StrMode mode) {
    this.srcReg = srcReg;
    this.addr = addr;
    this.mode = mode;
  }

  public STR(Register srcReg, Addressing addr) {
    this(srcReg, addr, StrMode.STR);
  }

  @Override
  public String assemble() {
    return mode.name() + " " + srcReg + ", " + addr;
  }

  /* STR{cond} <Rd>, <a_mode2> */
  public enum StrMode {STR, STRB}
}
