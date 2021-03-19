package backend.common.arithmeticLogic;

import backend.Instruction;
import backend.arm.instructions.ARMInstruction;
import frontend.node.expr.ExprNode;
import frontend.node.expr.UnopNode.Unop;
import java.util.List;
import utils.backend.register.Register;

public interface UnopAssemble {
  List<Instruction> unopAssemble(Register rd, Register rn, Unop op, ExprNode expr);
}
