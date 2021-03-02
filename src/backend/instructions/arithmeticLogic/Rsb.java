package backend.instructions.arithmeticLogic;

import backend.instructions.operand.Operand2;
import utils.backend.Cond;
import utils.backend.Register;

public class Rsb extends ArithmeticLogic {

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
