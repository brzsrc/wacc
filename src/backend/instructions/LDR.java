package backend.instructions;

import backend.instructions.addressing.Addressing;
import utils.backend.register.Register;

public class LDR extends Instruction {

  private final Register register;
  private final Addressing addr;
  private final LdrMode mode;
  public LDR(Register register, Addressing addr, LdrMode mode) {
    this.register = register;
    this.addr = addr;
    this.mode = mode;
  }

  public LDR(Register register, Addressing addr) {
    this(register, addr, LdrMode.LDR);
  }

  @Override
  public String assemble() {
    StringBuilder str = new StringBuilder();
    str.append(register + ", ");
    str.append(addr);
    return mode.name() + " " + str.toString();
  }

  /* LDR{cond} <Rd>, <a_mode2> */
  public enum LdrMode {LDR, LDRB, LDRSB, LDREQ, LDRNE, LDRLT, LDRCS}
}
