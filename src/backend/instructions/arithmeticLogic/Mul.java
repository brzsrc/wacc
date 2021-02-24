package backend.instructions.arithmeticLogic;

import backend.instructions.operand.Operand2;
import utils.backend.Register;

public class Mul extends ArithmeticLogic {

  public Mul(Register rd, Register rn, Operand2 operand2) {
    super(rd, rn, operand2);
  }

  @Override
  public String assemble() {
    return "MUL " + Rd + ", " + Rn + ", " + operand2;
  }
}
