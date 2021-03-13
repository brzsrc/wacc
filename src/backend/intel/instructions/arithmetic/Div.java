package backend.intel.instructions.arithmetic;

import backend.common.address.Immediate;
import utils.Utils;
import utils.backend.register.Register;

public class Div extends IntelArithmeticLogic {

  protected Div(Register rd) {
    super(rd, null);
  }

  @Override
  public String assemble() {
    return "idiv" + Utils.calculateSize(rd.asIntelRegister().getSize()) + " " + rd;
  }
}
