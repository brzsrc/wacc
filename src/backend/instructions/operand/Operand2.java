package backend.instructions.operand;

import java.util.ArrayList;
import java.util.List;

import utils.backend.Register;

/* operand specified as Table 1-14 in ARM spec */
public class Operand2 {

    public enum Operand2Operator { LSL, LSR, ASR, ROR, RRX, NONE }

    private Immediate immed;
    private Operand2Operator operator;
    private Register Rm;

    public Operand2(Register Rm, Operand2Operator operator, Immediate immed) {
        this.immed = immed;
        this.operator = operator;
        this.Rm = Rm;
    }

    public Operand2(Immediate immed) {
        this(null, Operand2Operator.NONE, immed);
    }

    public Operand2(Register Rm) {
        this(Rm, Operand2Operator.NONE, null);
    }

    public Operand2(Register Rm, Operand2Operator operator) {
        this(Rm, operator, null);
    }

    @Override
    public String toString() {
        List<String> res = new ArrayList<>();
        /* TODO: need better code quality here */
        if (Rm != null) res.add(Rm.toString());
        if (operator != Operand2Operator.NONE) res.add(operator.toString());
        res.add(immed.toString());
        return res.stream().reduce((i, j) -> i + " " + j).orElse("");
    }
    
}
