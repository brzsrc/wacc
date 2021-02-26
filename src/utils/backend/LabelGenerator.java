package utils.backend;

import backend.directives.Label;

public class LabelGenerator {

  private String header;
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
