package utils.backend;

import backend.instructions.Label;

public class LabelGenerator {
  private int labelCount;

  public LabelGenerator() {
    labelCount = 0;
  }

  public Label getLabel() {
    return new Label(labelCount++);
  }
}
