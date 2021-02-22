package backend.instructions.arithmeticLogic;

import backend.instructions.Instruction;
import backend.instructions.operand.Operand2;
import utils.backend.PseudoRegister;

public class Sub extends ArithmeticLogic {

  public Sub(PseudoRegister rd, PseudoRegister rn, Operand2 operand2) {
    super(rd, rn, operand2);
  }

  @Override
  public String assemble() {
    return "SUB " + Rd + ", " + Rn + ", " + operand2;
  }
}
