package backend.instructions.arithmeticLogic;

import java.util.List;

import backend.instructions.Instruction;
import backend.instructions.operand.Operand2;
import frontend.node.expr.BinopNode.Binop;
import utils.backend.Register;

public interface ArithmeticLogicAssemble {
    public List<Instruction> binopAssemble(Register rd, Register rn, Operand2 op2, Binop binop);
}
