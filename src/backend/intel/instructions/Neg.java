package backend.intel.instructions;

import utils.Utils;
import utils.backend.register.Register;

public class Neg implements IntelInstruction {

  Register reg;

  public Neg(Register reg) {
    this.reg = reg;
  }

  @Override
  public String assemble() {
    return "neg" + Utils.calculateSize(reg.asIntelRegister().getSize()) + " " + reg;
  }
}
