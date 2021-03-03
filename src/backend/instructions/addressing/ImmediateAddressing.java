package backend.instructions.addressing;

import backend.instructions.operand.Immediate;
import backend.instructions.operand.Immediate.BitNum;

public class ImmediateAddressing extends Addressing {
    private Immediate immed;

    public ImmediateAddressing(int val) {
        this.immed = new Immediate(val, BitNum.CONST16);
    }

    public Immediate getImmed() {
        return immed;
    }

    @Override
    public String toString() {
        if (immed.isChar()) {
          return immed.toString();
        }
        return "=" + String.valueOf(immed.getVal());
    }
}
