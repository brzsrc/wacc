package backend.arm.instructions.arithmeticLogic;

import backend.arm.instructions.addressing.Operand2;
import backend.common.address.Address;
import utils.backend.register.Register;

public class Mul extends ARMArithmeticLogic {

  /* MUL{cond}{S} <Rd>, <Rn>, <operand2> */
  public Mul(Register rd, Register rn, Address operand2) {
    super(rd, rn, operand2);
  }

  @Override
  public String assemble() {
    return "MUL " + rd + ", " + rn + ", " + addr;
  }
}
