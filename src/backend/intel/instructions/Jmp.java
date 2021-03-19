package backend.intel.instructions;

import backend.common.JmpInstruction;
import utils.backend.Cond;

public class Jmp extends JmpInstruction implements IntelInstruction {

  public Jmp(String label) {
    super(label);
  }

  public Jmp(Cond cond, String label) {
    super(cond, label);
  }

  @Override
  public String assemble() {
    return "j" + (cond.equals(Cond.NULL) ? "mp" : cond.name().toLowerCase()) + " " + label.getName();
  }
}
