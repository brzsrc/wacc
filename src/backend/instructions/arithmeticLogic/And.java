package backend.instructions.arithmeticLogic;

import backend.instructions.operand.Operand2;
import utils.backend.register.Register;

public class And extends ArithmeticLogic {
  /* AND{cond}{S} <Rd>, <Rn>, <operand2> */
  public And(Register rd, Register rn, Operand2 operand2) {
    super(rd, rn, operand2);
  }

  @Override
  public String assemble() {
    return "AND " + Rd + ", " + Rn + ", " + operand2;
  }
}
