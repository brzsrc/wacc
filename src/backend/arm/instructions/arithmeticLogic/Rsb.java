package backend.arm.instructions.arithmeticLogic;

import backend.arm.instructions.addressing.Operand2;
import utils.backend.register.Register;

public class Rsb extends ARMArithmeticLogic {

  private final RsbMode mode;

  public Rsb(Register rd, Register rn, Operand2 operand2) {
    super(rd, rn, operand2);
    mode = RsbMode.RSBS;
  }

  @Override
  public String assemble() {
    return mode + " " + rd + ", " + rn + ", " + addr;
  }

  /* RSB{cond}{S} <Rd>, <Rn>, <operand2> */
  public enum RsbMode {RSB, RSBS}
}
