package backend.intel.instructions;

import backend.common.CmpInstruction;
import backend.common.address.Address;
import utils.backend.register.Register;

public class Cmp extends CmpInstruction implements IntelInstruction {

  public Cmp(Register rs, Register rd) {
    super(rs, rd);
  }

  public Cmp(Register rs, Address addr) {
    super(rs, addr);
  }

  @Override
  public String assemble() {
    return null;
  }
}
