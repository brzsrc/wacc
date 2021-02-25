package backend.instructions.arithmeticLogic;

import backend.instructions.operand.Operand2;
import frontend.node.expr.BinopNode.Binop;
import utils.backend.Register;

public class Sub extends ArithmeticLogic {

  public Sub(Register rd, Register rn, Operand2 operand2) {
    super(Binop.MINUS, rd, rn, operand2);
  }

  @Override
  public String assemble() {
    return "SUB " + Rd + ", " + Rn + ", " + operand2;
  }
}
