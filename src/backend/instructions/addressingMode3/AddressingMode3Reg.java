package backend.instructions.addressingMode3;

import utils.backend.Register;

public class AddressingMode3Reg extends AddressingMode3{
  /*
     Register offset  [<Rn>,   +/- <Rm>]
       Pre-indexed    [<Rn>,   +/- <Rm>]!
       Post-indexed   [<Rn>],  +/- <Rm>
  */

  private AddressingMode mode;
  private boolean isMinus;
  private Register reg;


  public AddressingMode3Reg(AddressingMode mode, boolean isMinus, Register reg) {
    super(mode, isMinus);
    this.reg = reg;
  }
}
