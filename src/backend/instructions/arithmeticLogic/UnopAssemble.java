package backend.instructions.arithmeticLogic;

import backend.instructions.Instruction;
import java.util.List;
import utils.backend.register.Register;

public interface UnopAssemble {
  public List<Instruction> unopAssemble(Register rd, Register rn);
}
