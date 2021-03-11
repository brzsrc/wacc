package backend.intel.instructions.arithmetic;

import backend.arm.instructions.addressing.Operand2;
import backend.common.arithmeticLogic.ArithmeticLogic;
import backend.intel.instructions.IntelInstruction;
import utils.backend.register.Register;

public abstract class IntelArithmeticLogic extends ArithmeticLogic implements IntelInstruction {

  protected IntelArithmeticLogic(Register rd,
      Register rn,
      Operand2 operand2) {
    super(rd, rn, operand2);
  }
}
