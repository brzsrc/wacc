package backend.intel.instructions;

import backend.common.MovInstruction;
import backend.common.address.Address;
import utils.backend.register.Register;
import utils.backend.register.intel.IntelConcreteRegister;

public class Mov extends MovInstruction implements IntelInstruction {

  public Mov(Register rs, Register rd, Address operand2) {
    super(rs, rd, operand2);
  }

  public Mov(Address operand2, Register Rd) {
    super(Rd, operand2);
  }

  public Mov(Register Rs, Register Rd) {
    super(Rs, Rd);
  }

  public Mov(Register Rs, Address operand2) {
    super(operand2, Rs);
  }

  @Override
  public String assemble() {
    return null;
  }
}
