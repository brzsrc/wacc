package backend.instructions.operand;

import utils.backend.Register;

/* operand specified as Table 1-14 in ARM spec */
public class Operand2 {

    public enum Operand2Operator { LSL, LSR, ASR, ROR, RRX }

    private Immediate immed;
    private Operand2Operator operator;
    private Register register;

    public Operand2(Register register, Operand2Operator operator, Immediate immed) {
        this.immed = immed;
        this.operator = operator;
        this.register = register;
    }

    public Operand2(Immediate immed) {
        this(null, null, immed);
    }

    public Operand2(Register register) {
        this(register, null, null);
    }

    public Operand2(Register register, Operand2Operator operator) {
        this(register, operator, null);
    }
    
}
