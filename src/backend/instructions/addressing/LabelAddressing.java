package backend.instructions.addressing;

import backend.instructions.Label;

public class LabelAddressing extends Addressing {
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
