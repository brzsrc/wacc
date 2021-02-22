package backend.instructions.arithmeticLogic;

import backend.instructions.operand.Operand2;
import utils.backend.PseudoRegister;

public class Mul extends ArithmeticLogic {

  public Mul(PseudoRegister rd, PseudoRegister rn, Operand2 operand2) {
    Rd = rd;
    Rn = rn;
    this.operand2 = operand2;
  }

  @Override
  public String assemble() {
    return "MUL " + Rd + ", " + Rn + ", " + operand2;
  }
}
