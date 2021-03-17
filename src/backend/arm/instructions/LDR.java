package backend.arm.instructions;

import backend.common.address.Address;
import utils.backend.register.Register;

public class LDR implements ARMInstruction {

  private final Register register;
  private final Address addr;
  private final LdrMode mode;
  public LDR(Register register, Address addr, LdrMode mode) {
    this.register = register;
    this.addr = addr;
    this.mode = mode;
  }

  public LDR(Register register, Address addr) {
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
