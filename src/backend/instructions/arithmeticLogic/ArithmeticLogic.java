package backend.instructions.arithmeticLogic;

import backend.instructions.BL;
import backend.instructions.Cmp;
import backend.instructions.Instruction;
import backend.instructions.LDR;
import backend.instructions.Mov;
import backend.instructions.Mov.MovType;
import backend.instructions.addressing.AddressingMode2;
import backend.instructions.addressing.AddressingMode2.AddrMode2;
import backend.instructions.operand.Immediate;
import backend.instructions.operand.Immediate.BitNum;
import backend.instructions.operand.Operand2;
import frontend.node.expr.BinopNode.Binop;
import frontend.node.expr.UnopNode.Unop;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import utils.backend.Cond;
import utils.backend.register.ARMConcreteRegister;
import utils.backend.register.ARMRegisterLabel;
import utils.backend.register.Register;

public abstract class ArithmeticLogic extends Instruction {

  /* Instruction sets for binop and unop operations */

  public static final BinopAssemble BasicBinopAsm = (rd, rn, op2, b) -> {
    Map<Binop, Instruction> m = Map.of(
        Binop.PLUS, new Add(rd, rn, op2, Cond.S),
        Binop.MINUS, new Sub(rd, rn, op2, Cond.S),
        Binop.MUL, new SMull(rd, rn, op2),
        Binop.AND, new And(rd, rn, op2),
        Binop.OR, new Or(rd, rn, op2),
        Binop.BITAND, new And(rd, rn, op2),
        Binop.BITOR, new Or(rd, rn, op2)
    );
    return List.of(m.get(b));
  };

  public static final BinopAssemble DivModAsm = (rd, rn, op2, b) -> {
    /* op2 will not be used here */
    List<Instruction> list = new ArrayList<>();

    Register r0 = new ARMConcreteRegister(ARMRegisterLabel.R0);
    Register r1 = new ARMConcreteRegister(ARMRegisterLabel.R1);
    Operand2 dividend = new Operand2(rd);

    list.add(new Mov(r0, dividend));
    list.add(new Mov(r1, op2));
    list.add(new BL("p_check_divide_by_zero"));
    if (b.equals(Binop.DIV)) {
      list.add(new BL("__aeabi_idiv"));
      list.add(new Mov(rd, new Operand2(r0)));
    } else if (b.equals(Binop.MOD)) {
      list.add(new BL("__aeabi_idivmod"));
      /* we only put the remainder in the register and discard the quotient */
      list.add(new Mov(rd, new Operand2(r1)));
    }

    return list;
  };

  public static final BinopAssemble CmpAsm = (rd, rn, op2, b) -> {
    List<Instruction> list = new ArrayList<>();

    Operand2 one = new Operand2(new Immediate(1, BitNum.CONST8));
    Operand2 zero = new Operand2(new Immediate(0, BitNum.CONST8));

    list.add(new Cmp(rd, op2));
    /* default as false, set as true in following check */
    list.add(new Mov(rd, zero, MovType.MOV));
    list.add(new Mov(rd, one, Mov.binOpMovMap.get(b)));

    return list;
  };

  public static final Map<Binop, BinopAssemble> binopInstruction = Map.ofEntries(
      new AbstractMap.SimpleEntry<Binop, BinopAssemble>(Binop.PLUS, BasicBinopAsm),
      new AbstractMap.SimpleEntry<Binop, BinopAssemble>(Binop.MINUS, BasicBinopAsm),
      new AbstractMap.SimpleEntry<Binop, BinopAssemble>(Binop.MUL, BasicBinopAsm),
      new AbstractMap.SimpleEntry<Binop, BinopAssemble>(Binop.AND, BasicBinopAsm),
      new AbstractMap.SimpleEntry<Binop, BinopAssemble>(Binop.OR, BasicBinopAsm),
      new AbstractMap.SimpleEntry<Binop, BinopAssemble>(Binop.DIV, DivModAsm),
      new AbstractMap.SimpleEntry<Binop, BinopAssemble>(Binop.MOD, DivModAsm),
      new AbstractMap.SimpleEntry<Binop, BinopAssemble>(Binop.GREATER, CmpAsm),
      new AbstractMap.SimpleEntry<Binop, BinopAssemble>(Binop.GREATER_EQUAL, CmpAsm),
      new AbstractMap.SimpleEntry<Binop, BinopAssemble>(Binop.LESS, CmpAsm),
      new AbstractMap.SimpleEntry<Binop, BinopAssemble>(Binop.LESS_EQUAL, CmpAsm),
      new AbstractMap.SimpleEntry<Binop, BinopAssemble>(Binop.EQUAL, CmpAsm),
      new AbstractMap.SimpleEntry<Binop, BinopAssemble>(Binop.INEQUAL, CmpAsm)
  );

  public static final UnopAssemble ArrayLenAsm = (rd, rn) -> {
    List<Instruction> list = new ArrayList<>();
    list.add(new LDR(rd, new AddressingMode2(AddrMode2.OFFSET, rd)));
    return list;
  };

  public static UnopAssemble NegationAsm = (rd, rn) -> {
    List<Instruction> list = new ArrayList<>();
    list.add(new Rsb(rd, rn, new Operand2(new Immediate(0, BitNum.CONST8))));
    return list;
  };

  public static UnopAssemble LogicNotAsm = (rd, rn) -> {
    List<Instruction> list = new ArrayList<>();
    list.add(new Xor(rd, rn, new Operand2(new Immediate(1, BitNum.CONST8))));
    return list;
  };

  public static UnopAssemble OrdAsm = (rd, rn) -> {
    List<Instruction> list = new ArrayList<>();
    return list;
  };

  public static UnopAssemble ChrAsm = (rd, rn) -> {
    List<Instruction> list = new ArrayList<>();
    return list;
  };

  public static UnopAssemble ComplementAsm = (rd, rn) -> {
    List<Instruction> list = new ArrayList<>();
    list.add(new Xor(rd, rn, new Operand2(new Immediate(-1, BitNum.CONST8))));
    return list;
  };

  public static final Map<Unop, UnopAssemble> unopInstruction = Map.ofEntries(
      new AbstractMap.SimpleEntry<Unop, UnopAssemble>(Unop.LEN, ArrayLenAsm),
      new AbstractMap.SimpleEntry<Unop, UnopAssemble>(Unop.MINUS, NegationAsm),
      new AbstractMap.SimpleEntry<Unop, UnopAssemble>(Unop.NOT, LogicNotAsm),
      new AbstractMap.SimpleEntry<Unop, UnopAssemble>(Unop.ORD, OrdAsm),
      new AbstractMap.SimpleEntry<Unop, UnopAssemble>(Unop.CHR, ChrAsm),
      new AbstractMap.SimpleEntry<Unop, UnopAssemble>(Unop.COMPLEMENT, ComplementAsm)
  );

  protected Register Rd, Rn;
  protected Operand2 operand2;

  protected ArithmeticLogic(Register rd, Register rn, Operand2 operand2) {
    Rd = rd;
    Rn = rn;
    this.operand2 = operand2;
  }
}
