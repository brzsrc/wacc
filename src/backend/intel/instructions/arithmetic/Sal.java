package backend.intel.instructions.arithmetic;

import backend.intel.instructions.address.IntelImmediate;
import utils.backend.register.intel.IntelConcreteRegister;

public class Sal extends IntelArithmeticLogic {

  public Sal(IntelImmediate immed, IntelConcreteRegister rd) {
    super(immed, rd);
  }

  @Override
  public String assemble() {
    return null;
  }
}
