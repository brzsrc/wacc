package utils.backend;

import backend.common.LabelInstruction;
import java.lang.reflect.InvocationTargetException;

public class LabelGenerator<T extends LabelInstruction> {

  private final String header;
  private final Class<T> className;
  private int labelCount;

  public LabelGenerator(String header, Class<T> className) {
    this.header = header;
    this.className = className;
    labelCount = 0;
  }

  public T getLabel() {
    T label = null;
    try {
      String name = header + labelCount++;
      label = className.getConstructor(String.class).newInstance(name);
    } catch (IllegalAccessException | NoSuchMethodException |
        InstantiationException | InvocationTargetException e) {
      e.printStackTrace();
    }

    return label;
  }
}
