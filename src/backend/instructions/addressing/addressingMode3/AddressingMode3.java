package backend.instructions.addressing.addressingMode3;

import backend.instructions.addressing.Addressing;

public abstract class AddressingMode3 extends Addressing {

  public enum AddressingMode {
    OFFSET, PRE_INDEX, POST_INDEX
  }

  private AddressingMode mode;
  private boolean isMinus;

  public AddressingMode3(AddressingMode mode, boolean isMinus) {
    this.mode = mode;
    this.isMinus = isMinus;
  }
}
