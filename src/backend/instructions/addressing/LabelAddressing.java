package backend.instructions.addressing;

import backend.instructions.Label;

public class LabelAddressing extends Addressing {
  private final Label label;

  public LabelAddressing(Label label) {
    this.label = label;
  }

  public Label getLabel() {
    return label;
  }
}
