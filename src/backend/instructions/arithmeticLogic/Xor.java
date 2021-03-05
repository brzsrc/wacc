package backend.instructions.arithmeticLogic;

import backend.instructions.operand.Operand2;
import utils.backend.register.Register;

public class Xor extends ArithmeticLogic {

  /* EOR{cond}{S} <Rd>, <Rn>, <operand2> */
  public Xor(Register rd, Register rn, Operand2 operand2) {
    super(rd, rn, operand2);
  }

  @Override
  public String assemble() {
    return "EOR " + Rd + ", " + Rn + ", " + operand2;
  }
}
