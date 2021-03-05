package backend.instructions;

import backend.instructions.addressing.Addressing;
import utils.backend.register.Register;

public class STR extends Instruction {
  /* STR{cond} <Rd>, <a_mode2> */
  public enum StrMode { STR, STRB }

  private Register srcReg;
  private Addressing addr;
  private StrMode mode;

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
}
