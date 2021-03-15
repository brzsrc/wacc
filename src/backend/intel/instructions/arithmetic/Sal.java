package backend.intel.instructions.arithmetic;

import backend.intel.instructions.address.IntelImmediate;
import utils.Utils.IntelInstructionSize;
import utils.backend.register.intel.IntelConcreteRegister;

public class Sal extends IntelArithmeticLogic {

  public Sal(int i, IntelInstructionSize size, IntelConcreteRegister rd) {
    super(i, size, rd);
  }

  @Override
  public String assemble() {
    return assembleArithmeticLogic("sal");
  }
}
