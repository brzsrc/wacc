package backend.intel.instructions.arithmetic;

import backend.intel.instructions.address.IntelImmediate;
import utils.backend.register.intel.IntelConcreteRegister;

public class Sal extends IntelArithmeticLogic {

  public Sal(int i, IntelConcreteRegister rd) {
    super(i, rd);
  }

  @Override
  public String assemble() {
    return assembleArithmeticLogic("sal");
  }
}
