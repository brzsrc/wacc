package backend.instructions.arithmeticLogic;

import backend.instructions.Instruction;
import backend.instructions.operand.Operand2;
import frontend.node.expr.BinopNode.Binop;
import java.util.List;
import utils.backend.register.Register;

public interface BinopAssemble {

  List<Instruction> binopAssemble(Register rd, Register rn, Operand2 op2, Binop binop);
}
