package backend.arm.instructions.arithmeticLogic;

import backend.arm.instructions.addressing.Operand2;
import utils.backend.register.Register;

public class SMull extends ARMArithmeticLogic {

  /* SMULL{cond}{S} <RdLo>, <RdHi>, <Rm>, <Rs> */
  public SMull(Register rd, Register rn, Operand2 op2) {
    super(rd, rn, op2);
  }

  @Override
  public String assemble() {
    return "SMULL " + Rd + ", " + operand2 + ", " + Rd + ", " + operand2;
  }
}
