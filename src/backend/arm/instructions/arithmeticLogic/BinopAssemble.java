package backend.arm.instructions.arithmeticLogic;

import backend.arm.instructions.ARMInstruction;
import backend.arm.instructions.operand.Operand2;
import frontend.node.expr.BinopNode.Binop;
import java.util.List;
import utils.backend.register.Register;

public interface BinopAssemble {

  List<ARMInstruction> binopAssemble(Register rd, Register rn, Operand2 op2, Binop binop);
}
