package backend.intel.instructions;

import backend.common.CmpInstruction;
import backend.common.address.Address;
import utils.backend.register.Register;

public class Cmp extends CmpInstruction implements IntelInstruction {

  public Cmp(Register Rd, Address operand2) {
    super(Rd, operand2);
  }

  @Override
  public String assemble() {
    return null;
  }
}
