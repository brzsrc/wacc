package backend.instructions.arithmeticLogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import backend.instructions.BL;
import backend.instructions.Cmp;
import backend.instructions.Instruction;
import backend.instructions.Mov;
import backend.instructions.Mov.MovType;
import backend.instructions.operand.Immediate;
import backend.instructions.operand.Operand2;
import backend.instructions.operand.Immediate.BitNum;
import frontend.node.expr.BinopNode.Binop;
import frontend.node.expr.UnopNode.Unop;
import utils.backend.ARMConcreteRegister;
import utils.backend.ARMRegisterLabel;
import utils.backend.Register;

public abstract class ArithmeticLogic extends Instruction {

  public static final ArithmeticLogicAssemble BasicBinopAsm = (rd, rn, op2, b) -> {
    /* TODO: need better code quality here */
    Map<Binop, ArithmeticLogic> m = Map.of(
      Binop.PLUS, new Add(rd, rn, op2),
      Binop.MINUS, new Sub(rd, rn, op2),
      Binop.MUL, new Mul(rd, rn, op2),
      Binop.AND, new And(rd, rn, op2),
      Binop.OR, new Or(rd, rn, op2)
    );

    return List.of(m.get(b));
  };

  public static final ArithmeticLogicAssemble DivModAsm = (rd, rn, op2, b) -> {
    /* notice that op2 will not be used heree */
    /* TODO: need better code here */
    List<Instruction> list = new ArrayList<>();

    Register r0 = new ARMConcreteRegister(ARMRegisterLabel.R0);
    Register r1 = new ARMConcreteRegister(ARMRegisterLabel.R1);
    Operand2 dividend = new Operand2(rd);
    Operand2 dividor = new Operand2(rn);
   
    list.add(new Mov(r0, dividend));
    list.add(new Mov(r1, dividor));
    list.add(new BL("p_check_divide_by_zero"));
    if (b.equals(Binop.DIV)) {
      list.add(new BL("__aeabi_idiv"));
      list.add(new Mov(r0, dividend));
    } else if (b.equals(Binop.MOD)) {
      list.add(new BL("__aeabi_idivmod"));
      /* we only put the remainder in the register and discard the quotient */
      list.add(new Mov(r1, dividend));
    }

    return list;
  };

  public static final ArithmeticLogicAssemble CmpAsm = (rd, rn, op2, b) -> {
    List<Instruction> list = new ArrayList<>();

    Operand2 operand2 = new Operand2(rn);
    Operand2 one = new Operand2(new Immediate(1, BitNum.CONST8));
    Operand2 zero = new Operand2(new Immediate(0, BitNum.CONST8));
    
    list.add(new Cmp(rd, operand2));

    if (b.equals(Binop.GREATER)) {
      list.add(new Mov(rd, one, MovType.GT));
      list.add(new Mov(rd, zero, MovType.LE));
    } else if (b.equals(Binop.GREATER_EQUAL)) {
      list.add(new Mov(rd, one, MovType.GE));
      list.add(new Mov(rd, zero, MovType.LT));
    } else if (b.equals(Binop.LESS)) {
      list.add(new Mov(rd, one, MovType.LT));
      list.add(new Mov(rd, zero, MovType.GE));
    } else if (b.equals(Binop.LESS_EQUAL)) {
      list.add(new Mov(rd, one, MovType.LE));
      list.add(new Mov(rd, zero, MovType.GT));
    }

    return list;
  };

  public static final ArithmeticLogicAssemble EqualityAsm = (rd, rn, op2, b) -> {
    List<Instruction> list = new ArrayList<>();

    Operand2 reg2op2 = new Operand2(rn);
    Operand2 one = new Operand2(new Immediate(1, BitNum.CONST8));
    Operand2 zero = new Operand2(new Immediate(0, BitNum.CONST8));

    list.add(new Cmp(rd, reg2op2));
    if (b.equals(Binop.EQUAL)) {
      list.add(new Mov(rd, one, MovType.EQ));
      list.add(new Mov(rd, zero, MovType.NE));
    } else if (b.equals(Binop.INEQUAL)) {
      list.add(new Mov(rd, zero, MovType.EQ));
      list.add(new Mov(rd, one, MovType.NE));
    }

    return list;
  };

  /* TODO: need better code quality here */
  public static final Map<Binop, ArithmeticLogicAssemble> binopInstruction = new HashMap<>(){{
    put(Binop.PLUS, BasicBinopAsm);
    put(Binop.MINUS, BasicBinopAsm);
    put(Binop.MUL, BasicBinopAsm);
    put(Binop.AND, BasicBinopAsm); 
    put(Binop.OR, BasicBinopAsm);
    put(Binop.DIV, DivModAsm);
    put(Binop.MOD, DivModAsm);
    put(Binop.GREATER, CmpAsm); 
    put(Binop.GREATER_EQUAL, CmpAsm);
    put(Binop.LESS, CmpAsm);
  }};

  private Binop binop;
  private Unop unop;
  protected Register Rd, Rn;
  protected Operand2 operand2;

  public ArithmeticLogic(Register rd, Register rn, Operand2 operand2) {
    Rd = rd;
    Rn = rn;
    this.operand2 = operand2;
  }

  public ArithmeticLogic(Binop binop, Register rd, Register rn, Operand2 operand2) {
    this(rd, rn, operand2);
    this.binop = binop;
  }

  public ArithmeticLogic(Unop unop, Register rd, Register rn, Operand2 operand2) {
    this(rd, rn, operand2);
    this.unop = unop;
  }
}
