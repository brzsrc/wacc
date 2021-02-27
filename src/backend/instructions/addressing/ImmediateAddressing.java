package backend.instructions.addressing;

import backend.instructions.operand.Immediate;

public class ImmediateAddressing extends Addressing {
    private Immediate immed;

    public ImmediateAddressing(Immediate immed) {
        this.immed = immed;
    }

    public Immediate getImmed() {
        return immed;
    }

    @Override
    public String toString() {
        return "=" + immed.getVal();
    }
}
