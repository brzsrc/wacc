package backend.intel.instructions.address;

import backend.common.address.Immediate;
import backend.intel.instructions.Label;
import utils.Utils.IntelInstructionSize;

public class IntelImmediate extends Immediate {

  private final IntelInstructionSize size;

  public IntelImmediate(int val, IntelInstructionSize size) {
    super(val);
    this.size = size;
  }

  public IntelImmediate(Label label) {
    super(label);
    size = null;
  }

  public IntelInstructionSize getSize() {
    return this.size;
  }

  @Override
  public String assemble() {
    return label == null ? "$" + val : "." + label.getName();
  }
}
