package backend.common.arithmeticLogic;

import backend.common.address.Address;
import backend.common.address.Immediate;
import backend.intel.instructions.address.IntelImmediate;
import utils.Utils.IntelInstructionSize;
import utils.backend.register.Register;

public abstract class ArithmeticLogic {
  protected Register rd, rn;
  protected Address addr;
  protected Immediate immed;

  private ArithmeticLogic(Register rd, Register rn, Address addr, Immediate immed) {
    this.rd = rd;
    this.rn = rn;
    this.addr = addr;
    this.immed = immed;
  }

  public ArithmeticLogic(Register rd, Register rn, Address addr) {
    this(rd, rn, addr, null);
  }

  public ArithmeticLogic(Register rd, Register rn) {
    this(rd, rn, null, null);
  }

  public ArithmeticLogic(int val, IntelInstructionSize size, Register rd) {
    this(rd, null, null, new IntelImmediate(val, size));
  }
}
