package backend.instructions.addressing;

import utils.backend.Register;

public class RegAddressing extends Addressing {
  /* for simple case [<Rn>] */
  private final Register reg;

  public RegAddressing(Register reg) {
    this.reg = reg;
  }

  public Register getReg() {
    return reg;
  }

  @Override
  public String toString() {
    return reg.toString();
  }
}
