package backend.instructions;

import backend.instructions.operand.Operand2;
import utils.backend.Register;

public class Cmp extends Instruction {

<<<<<<< HEAD
  private final Register Rd;
  private final Operand2 operand2;

  public Cmp(Register Rd, Operand2 operand2) {
    this.Rd = Rd;
=======
  private Register Rn;
  private Operand2 operand2;

  public Cmp(Register rn, Operand2 operand2) {
    Rn = rn;
>>>>>>> develop
    this.operand2 = operand2;
  }

  @Override
  public String assemble() {
<<<<<<< HEAD
    return "CMP " + Rd + ", " + operand2;
=======
    return "CMP " + Rn + ", " + operand2;
>>>>>>> develop
  }
}
