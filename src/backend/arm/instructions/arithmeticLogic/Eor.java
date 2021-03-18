package backend.arm.instructions.arithmeticLogic;

import backend.common.address.Address;
import utils.backend.register.Register;

public class Eor extends ARMArithmeticLogic {
  /* ORR{cond}{S} <Rd>, <Rn>, <operand2> */
  public Eor(Register rd, Register rn, Address operand2) {
    super(rd, rn, operand2);
  }

  @Override
  public String assemble() {
    return "EOR " + rd + ", " + rn + ", " + addr;
  }
}
