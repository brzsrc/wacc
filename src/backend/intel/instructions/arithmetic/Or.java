package backend.intel.instructions.arithmetic;

import backend.arm.instructions.addressing.Operand2;
import utils.backend.register.Register;

public class Or extends IntelArithmeticLogic {

  protected Or(Register rd,
      Register rn) {
    super(rd, rn);
  }

  @Override
  public String assemble() {
    return null;
  }
}
