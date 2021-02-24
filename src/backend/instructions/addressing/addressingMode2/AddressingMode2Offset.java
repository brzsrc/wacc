package backend.instructions.addressing.addressingMode2;

public class AddressingMode2 extends Addressing {

    enum AddrMode2Operator { LSL, LSR, ASR, ROR, RRX }

    enum AddrMode2 { OFFSET, PREINDEX, POSTINDEX }

    private AddrMode2 mode;
    private Register Rn;
    private Register Rm;
    private AddrMode2Operator operator;
    private Immediate immed;

    public AddressingMode2Offset(AddrMode2 mode, Register Rn, Register Rm, AddrMode2Operator operator, Immediate immed) {
        this.mode = mode;
        this.Rn = Rn;
        this.Rm = Rm;
        this.operator = operator;
        this.immed = immed;
    }

    public AddressingMode2Offset(AddrMode2 mode, Register Rn, Register Rm) {
        this(mode, Rn, Rm, null, null);
    }

    public AddressingMode2Offset(AddrMode2 mode, Register Rn, Immediate immed) {
        this(mode, Rn, null, null, immed);
    }

    public AddressingMode2Offset(AddrMode2 mode, Register Rn) {
        this(mode, Rn, null, null, null);
    }
}
