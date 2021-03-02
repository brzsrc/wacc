package backend.instructions.arithmeticLogic;

import backend.instructions.operand.Operand2;
import frontend.node.expr.BinopNode.Binop;
import utils.backend.Cond;
import utils.backend.Register;

public class Sub extends ArithmeticLogic {

  private Cond cond;

  public Sub(Register rd, Register rn, Operand2 operand2) {
    super(rd, rn, operand2);
    this.cond = Cond.NULL;
  }

  public Sub(Register rd, Register rn,
      Operand2 operand2, Cond cond) {
    super(rd, rn, operand2);
    this.cond = cond;
  }

  @Override
  public String assemble() {
    return "SUB" + cond + " " + Rd + ", " + Rn + ", " + operand2;
  }
}
