package backend.intel.instructions.arithmetic;

import backend.common.address.Address;
import backend.common.arithmeticLogic.ArithmeticLogic;
import backend.intel.instructions.IntelInstruction;
import utils.backend.register.Register;

public abstract class IntelArithmeticLogic extends ArithmeticLogic implements IntelInstruction {

  protected IntelArithmeticLogic(Register rd,
      Register rn,
      Address operand2) {
    super(rd, rn, operand2);
  }
}
