package backend.instructions.addressing.addressingMode3;

public abstract class AddressingMode3 extends Addressing {

  private AddressingMode mode;
  private boolean isMinus;

  public AddressingMode3(AddressingMode mode, boolean isMinus) {
    this.mode = mode;
    this.isMinus = isMinus;
  }
}
