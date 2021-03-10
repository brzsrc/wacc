package backend.arm.instructions.arithmeticLogic;

import backend.arm.instructions.operand.Operand2;
import utils.backend.register.Register;

public class Rsb extends ArithmeticLogic {

  private final RsbMode mode;

  public Rsb(Register rd, Register rn, Operand2 operand2) {
    super(rd, rn, operand2);
    mode = RsbMode.RSBS;
  }

  @Override
  public String assemble() {
    return mode + " " + Rd + ", " + Rn + ", " + operand2;
  }

  /* RSB{cond}{S} <Rd>, <Rn>, <operand2> */
  public enum RsbMode {RSB, RSBS}
}
