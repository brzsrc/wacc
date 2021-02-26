package backend.instructions.arithmeticLogic;

import backend.instructions.Instruction;
import backend.instructions.operand.Operand2;
import frontend.node.expr.UnopNode.Unop;
import java.util.List;
import utils.backend.Register;

public interface UnopAssemble {
  public List<Instruction> unopAssemble(Register rd, Register rn);
}
