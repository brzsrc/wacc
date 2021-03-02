package backend.instructions.arithmeticLogic;

import backend.instructions.operand.Operand2;
import frontend.node.expr.BinopNode.Binop;
import utils.backend.Cond;
import utils.backend.Register;

public class SMull extends ArithmeticLogic {

  protected Register Rn1, Rn2; 

  public SMull(Register rd1, Register rd2, Register rn1, Register rn2) {
    super(rd1, rd2, null);
  }

  @Override
  public String assemble() {
    return "SMULL " + Rd + ", " + Rn + ", " + Rd + ", " + Rn;
  }
}
