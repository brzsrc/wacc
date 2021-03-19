package backend.intel.instructions.arithmetic;

import backend.intel.instructions.IntelInstruction;
import utils.Utils;
import utils.backend.register.Register;

public class And extends IntelArithmeticLogic {

  protected And(Register rn,
      Register rd) {
    super(rn, rd);
  }

  @Override
  public String assemble() {
    return assembleArithmeticLogic("and");
  }
}
