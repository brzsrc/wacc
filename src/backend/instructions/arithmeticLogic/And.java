package backend.instructions.arithmeticLogic;

import backend.instructions.operand.Operand2;
import frontend.node.expr.BinopNode.Binop;
import utils.backend.Register;

public class And extends ArithmeticLogic {

  public And(Register rd, Register rn, Operand2 operand2) {
    super(Binop.AND, rd, rn, operand2);
  }

  @Override
  public String assemble() {
    return "AND " + Rd + ", " + Rn + ", " + operand2;
  }
}
