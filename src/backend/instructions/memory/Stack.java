package backend.instructions.memory;

import backend.instructions.Instruction;
import java.util.List;
import utils.backend.Register;

public interface Stack {
  public Instruction push(List<Register> reglist);
  public Instruction pop(List<Register> reglist);
  public boolean isFull();
}
