package backend.intel.instructions;

import utils.backend.register.Register;
import utils.backend.register.intel.IntelConcreteRegister;

public class Not implements IntelInstruction{

  Register reg;

  public Not(Register reg) {
    this.reg = reg;
  }

  @Override
  public String assemble() {
    return null;
  }
}
