package backend.intel.instructions.arithmetic;

import backend.arm.instructions.addressing.Operand2;
import backend.common.address.Address;
import backend.common.address.Immediate;
import utils.Utils;
import utils.backend.register.Register;
import utils.backend.register.intel.IntelConcreteRegister;
import utils.backend.register.intel.IntelConcreteRegisterAllocator;

public class Add extends IntelArithmeticLogic {

  public Add(Register rd, Register rn) {
    super(rd, rn);
  }

  public Add(int i, Register Rd) {
    super(i, Rd);
  }

  @Override
  public String assemble() {
    return super.assembleArithmeticLogic("add");
  }
}
