package backend.instructions;

import backend.instructions.addressing.addressingMode3.*;
import utils.backend.ARMConcreteRegister;

public class STR extends Instruction {

  private ARMConcreteRegister srcReg;
  private AddressingMode3 mode3;

  public STR(ARMConcreteRegister srcReg, AddressingMode3 mode3) {
    this.srcReg = srcReg;
    this.mode3 = mode3;
  }


  @Override
  public String assemble() {
    return null;
  }
}
