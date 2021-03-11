package backend.arm.instructions.arithmeticLogic;

import backend.arm.instructions.addressing.Operand2;
import utils.backend.register.Register;

public class Mul extends ARMArithmeticLogic {

  /* MUL{cond}{S} <Rd>, <Rn>, <operand2> */
  public Mul(Register rd, Register rn, Operand2 operand2) {
    super(rd, rn, operand2);
  }

  @Override
  public String assemble() {
    return "MUL " + Rd + ", " + Rn + ", " + operand2;
  }
}
