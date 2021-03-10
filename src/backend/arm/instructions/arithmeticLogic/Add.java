package backend.arm.instructions.arithmeticLogic;

import backend.arm.instructions.operand.Operand2;
import utils.backend.Cond;
import utils.backend.register.Register;

public class Add extends ArithmeticLogic {

  /* ADD{cond}{S} <Rd>, <Rn>, <operand2> */
  private final Cond cond;

  public Add(Register rd, Register rn,
      Operand2 operand2) {
    super(rd, rn, operand2);
    cond = Cond.NULL;
  }

  public Add(Register rd, Register rn,
      Operand2 operand2, Cond cond) {
    super(rd, rn, operand2);
    this.cond = cond;
  }

  @Override
  public String assemble() {
    return "ADD" + cond + " " + Rd + ", " + Rn + ", " + operand2;
  }

}
