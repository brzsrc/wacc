package backend.intel.instructions;

import backend.common.MovInstruction;
import backend.common.address.Address;
import utils.backend.register.Register;

public class Mov extends MovInstruction implements IntelInstruction {

  public Mov(Register Rd, Address operand2) {
    super(Rd, operand2);
  }

  @Override
  public String assemble() {
    return null;
  }
}
