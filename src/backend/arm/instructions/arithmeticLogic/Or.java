package backend.arm.instructions.arithmeticLogic;

import backend.arm.instructions.addressing.Operand2;
import utils.backend.register.Register;

public class Or extends ARMArithmeticLogic {

  /* ORR{cond}{S} <Rd>, <Rn>, <operand2> */
  public Or(Register rd, Register rn, Operand2 operand2) {
    super(rd, rn, operand2);
  }

  @Override
  public String assemble() {
    return "ORR " + Rd + ", " + Rn + ", " + operand2;
  }
}
