package backend.instructions;

import backend.instructions.addressing.Addressing;
import utils.backend.Register;

public class LDR extends Instruction {

  public enum LdrMode { LDR, LDRB, LDREQ, LDRNE, LDRLT, LDRCS }

  private Register register;
  private Addressing addr;
  private LdrMode mode;

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
}
