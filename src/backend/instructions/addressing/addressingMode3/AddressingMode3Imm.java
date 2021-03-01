package backend.instructions.addressing.addressingMode3;

import backend.instructions.operand.Immediate;

public class AddressingMode3Imm extends AddressingMode3 {
  /* Immediate offset [<Rn>,  #+/-<immed_8>]
       Pre-indexed    [<Rn>,  #+/-<immed_8>]!
       Post-indexed   [<Rn>], #+/-<immed_8>
  */
  private Immediate immediate;


  public AddressingMode3Imm(AddressingMode mode, boolean isMinus, Immediate immediate) {
    super(mode, isMinus);
    this.immediate = immediate;
  }
}
