package backend.instructions.arithmeticLogic;

import backend.instructions.operand.*;
import utils.backend.PseudoRegister;

public class Add extends ArithmeticLogic {

  public Add(PseudoRegister rd, PseudoRegister rn,
      Operand2 operand2) {
    super(rd, rn, operand2);
  }

  @Override
  public String assemble() {
    return "ADD " + Rd + ", " + Rn + ", " + operand2;
  }

}
