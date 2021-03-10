package utils.backend;

import backend.arm.instructions.Label;

public class LabelGenerator {

  private final String header;
  private int labelCount;

  public LabelGenerator(String header) {
    this.header = header;
    labelCount = 0;
  }

  public Label getLabel() {
    String name = header + labelCount++;
    return new Label(name);
  }
}
