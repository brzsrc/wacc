package backend.instructions.arithmeticLogic;

import backend.instructions.operand.Operand2;
import utils.backend.Register;

public class Xor extends ArithmeticLogic {

  public Xor(Register rd, Register rn, Operand2 operand2) {
    super(rd, rn, operand2);
  }

  @Override
  public String assemble() {
    return "XOR " + Rd + ", " + Rn + ", " + operand2;
  }
}
