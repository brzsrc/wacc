package backend.instructions.arithmeticLogic;

import backend.instructions.operand.*;
import frontend.node.expr.BinopNode.Binop;
import utils.backend.Cond;
import utils.backend.Register;

public class Add extends ArithmeticLogic {

  private Cond cond;

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
    return  "ADD" + cond + " " + Rd + ", " + Rn + ", " + operand2;
  }

}
