package backend.instructions.Operand;

import utils.backend.PseudoRegister;

/* operand specified as Table 1-14 in ARM spec */
public class Operand2 implements Operand {

    enum Operand2Operator { LSL, LSR, ASR, ROR, RRX }

    private Immediate immed;
    private Operand2Operator operator;
    private PseudoRegister register;

    public Operand2(PseudoRegister register, Operand2Operator operator, Immediate immed) {
        this.immed = immed;
        this.operator = operator;
        this.register = register;
    }

    public Operand2(Immediate immed) {
        this(null, null, immed);
    }

    public Operand2(PseudoRegister register) {
        this(register, null, null);
    }

    public Operand2(PseudoRegister register, Operand2Operator operator) {
        this(register, operator, null);
    }
    
}
