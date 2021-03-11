package backend.arm.instructions.addressing;

import backend.arm.instructions.Label;
import backend.common.address.Address;

public class LabelAddressing extends Address {

  /* used in case like: ldr r0 =msg */
  private final Label label;

  public LabelAddressing(Label label) {
    this.label = label;
  }

  public Label getLabel() {
    return label;
  }

  @Override
  public String toString() {
    return "=" + label.getName();
  }
}
