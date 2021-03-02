package backend.instructions.arithmeticLogic;

import backend.instructions.operand.Operand2;
import frontend.node.expr.BinopNode.Binop;
import utils.backend.Cond;
import utils.backend.Register;

public class SMull extends ArithmeticLogic {

  public SMull(Register rd, Register rn, Operand2 op2) {
    super(rd, rn, op2);
  }

  @Override
  public String assemble() {
    return "SMULL " + Rd + ", " + operand2 + ", " + Rd + ", " + operand2;
  }
}
