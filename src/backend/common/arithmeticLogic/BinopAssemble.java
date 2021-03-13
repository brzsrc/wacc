package backend.common.arithmeticLogic;

import backend.Instruction;
import backend.arm.instructions.ARMInstruction;
import backend.arm.instructions.addressing.Operand2;
import backend.common.address.Address;
import frontend.node.expr.BinopNode.Binop;
import java.util.List;
import utils.backend.register.Register;

public interface BinopAssemble {
  List<Instruction> binopAssemble(Register rd, Register rn, Address op2, Binop binop);
}
