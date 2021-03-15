package backend.intel.instructions.arithmetic;

import backend.arm.instructions.addressing.Operand2;
import backend.common.address.Address;
import backend.common.address.Immediate;
import utils.Utils;
import utils.Utils.IntelInstructionSize;
import utils.backend.register.Register;
import utils.backend.register.intel.IntelConcreteRegister;
import utils.backend.register.intel.IntelConcreteRegisterAllocator;

public class Add extends IntelArithmeticLogic {

  public Add(Register rn, Register rd) {
    super(rn, rd);
  }

  public Add(int i, IntelInstructionSize size, Register Rd) {
    super(i, size, Rd);
  }

  @Override
  public String assemble() {
    return super.assembleArithmeticLogic("add");
  }
}
