package backend.instructions.arithmeticLogic;

import backend.instructions.operand.Operand2;
import frontend.node.expr.BinopNode.Binop;
import utils.backend.Register;

public class Or extends ArithmeticLogic {

  public Or(Register rd, Register rn, Operand2 operand2) {
    super(rd, rn, operand2);
  }

  @Override
  public String assemble() {
    return "OR " + Rd + ", " + Rn + ", " + operand2;
  }
}
