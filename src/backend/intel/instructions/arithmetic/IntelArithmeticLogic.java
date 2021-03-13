package backend.intel.instructions.arithmetic;

import backend.Instruction;
import backend.common.arithmeticLogic.UnopAssemble;
import backend.common.address.Immediate;
import backend.common.arithmeticLogic.ArithmeticLogic;
import backend.common.arithmeticLogic.BinopAssemble;
import backend.intel.instructions.Cltd;
import backend.intel.instructions.Cmp;
import backend.intel.instructions.IntelInstruction;
import backend.intel.instructions.Mov;
import backend.intel.instructions.Neg;
import backend.intel.instructions.Not;
import backend.intel.instructions.Set;
import backend.intel.instructions.Set.IntelSetType;
import backend.intel.instructions.address.IntelAddress;
import frontend.node.expr.ArrayNode;
import frontend.node.expr.BinopNode.Binop;
import frontend.node.expr.UnopNode.Unop;
import java.util.List;
import java.util.Map;
import utils.backend.register.Register;

public abstract class IntelArithmeticLogic extends ArithmeticLogic implements IntelInstruction {

  public static final BinopAssemble intelBinopAsm = (rd, rn, notUsed, b) -> {
    Map<Binop, List<Instruction>> m = Map.of(
        Binop.PLUS, List.of(new Add(rn, rd)),
        Binop.MINUS, List.of(new Sub(rn, rd)),
        Binop.MUL, List.of(new Mul(rn, rd)),
        Binop.AND, List.of(new And(rn, rd)),
        Binop.OR, List.of(new Or(rn, rd)),
        Binop.DIV, List.of(new Cltd(), new Div(rd)),
        Binop.MOD, List.of(),
        Binop.EQUAL, List.of(new Cmp(rn, rd), new Set(rd, IntelSetType.E)),
        Binop.INEQUAL, List.of(new Cmp(rn, rd), new Set(rd, IntelSetType.NE)),
        Binop.GREATER, List.of(new Cmp(rn, rd), new Set(rd, IntelSetType.G))
    );

    m.put(Binop.LESS, List.of(new Cmp(rn, rd), new Set(rd, IntelSetType.L)));
    m.put(Binop.GREATER_EQUAL, List.of(new Cmp(rn, rd), new Set(rd, IntelSetType.GE)));
    m.put(Binop.LESS_EQUAL, List.of(new Cmp(rn, rd), new Set(rd, IntelSetType.LE)));

    return m.get(b);
  };

  public static final UnopAssemble intelUnopAsm = (rd, rn, op, expr) -> {
    int len = 0;

    if (op.equals(Unop.LEN)) {
      len = ((ArrayNode) expr).getLength();
    }

    Map<Unop, List<Instruction>> m = Map.of(
        Unop.LEN, List.of(new Mov(new IntelAddress(len), rd)),
        Unop.CHR, List.of(),
        Unop.MINUS, List.of(new Neg(rd)),
        Unop.NOT, List.of(new Not(rd)),
        Unop.ORD, List.of()
    );

    return m.get(op);
  };

  protected IntelArithmeticLogic(Register rd, Register rn) {
    super(rd, rn);
  }

  protected IntelArithmeticLogic(Immediate immed, Register rd) {
    super(immed, rd);
  }
}
