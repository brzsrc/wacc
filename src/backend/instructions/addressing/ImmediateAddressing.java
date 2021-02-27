package backend.instructions.addressing;

import backend.instructions.operand.Immediate;

public class ImmediateAddressing extends Addressing {
    private Immediate immed;
    private String dataAddr;

    public ImmediateAddressing(Immediate immed) {
        this.immed = immed;
    }

    public ImmediateAddressing(String dataAddr) {
        this.dataAddr = dataAddr;
    }

    public Immediate getImmed() {
        return immed;
    }

    public String getDataAddr() {
        return dataAddr;
    }
}
