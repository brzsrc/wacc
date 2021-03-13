package backend.common.address;

import backend.arm.instructions.addressing.ARMImmediate;
import backend.arm.instructions.addressing.ARMImmediate.BitNum;
import backend.common.LabelInstruction;
import backend.intel.instructions.address.IntelImmediate;

public abstract class Immediate {
  protected final int val;
  protected final LabelInstruction label;
  protected final boolean isChar;
  protected final boolean isLabel;

  public Immediate(int val) {
    this(val, null, false, false);
  }

  public Immediate(LabelInstruction label) {
    this(0, label, false, true);
  }

  public Immediate(int val, LabelInstruction label, boolean isChar, boolean isLabel) {
    this.val = val;
    this.label = label;
    this.isChar = isChar;
    this.isLabel = isLabel;
  }

  public ARMImmediate asArmImmediate() {
    return (ARMImmediate) this;
  }

  public IntelImmediate asIntelImmediate() {
    return (IntelImmediate) this;
  }

  public abstract String assemble();
}
