package backend.instructions.arithmeticLogic;

import backend.instructions.operand.Operand2;
import utils.backend.register.Register;

public class Rsb extends ArithmeticLogic {
  /* RSB{cond}{S} <Rd>, <Rn>, <operand2> */
  public enum RsbMode { RSB, RSBS }
  private RsbMode mode;

  public Rsb(Register rd, Register rn, Operand2 operand2) {
    super(rd, rn, operand2);
    mode = RsbMode.RSBS;
  }

  @Override
  public String assemble() {
    return mode + " " + Rd + ", " + Rn + ", " + operand2;
  }
}
