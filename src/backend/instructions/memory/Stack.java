package backend.instructions.memory;

import backend.instructions.Instruction;

public interface Stack {
  public Instruction push();
  public Instruction pop();
  public boolean isFull();
}
