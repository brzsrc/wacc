package backend.intel.instructions.arithmetic;

import backend.intel.instructions.IntelInstruction;
import utils.backend.register.Register;

public class And extends IntelArithmeticLogic {

  protected And(Register rd,
      Register rn) {
    super(rd, rn);
  }

  @Override
  public String assemble() {
    return null;
  }
}
