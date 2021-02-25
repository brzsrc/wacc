package backend.instructions.arithmeticLogic;

import backend.instructions.operand.*;
import utils.backend.Register;

public class Add extends ArithmeticLogic {

  public Add(Register rd, Register rn,
      Operand2 operand2) {
    super(rd, rn, operand2);
  }

  @Override
  public String assemble() {
    return "ADD " + Rd + ", " + Rn + ", " + operand2;
  }

}
