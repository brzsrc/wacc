package backend.intel.instructions.address;

import backend.common.address.Immediate;
import backend.intel.instructions.Label;

public class IntelImmediate extends Immediate {
  public IntelImmediate(int val) {
    super(val);
  }

  public IntelImmediate(Label label) {
    super(label);
  }

  @Override
  public String assemble() {
    return null;
  }
}
