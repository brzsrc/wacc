package backend.arm.instructions.arithmeticLogic;

import backend.arm.instructions.ARMInstruction;
import java.util.List;
import utils.backend.register.Register;

public interface UnopAssemble {
  List<ARMInstruction> unopAssemble(Register rd, Register rn);
}
