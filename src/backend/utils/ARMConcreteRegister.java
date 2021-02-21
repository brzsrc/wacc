package backend.utils;

/* internal labeling of registers. ST stands for Stack Pointer, 
   SLR stands for Subroutine Link Register, PC stands for Program Counter, 
   CPSR stands for Current Program Status Register */
enum ARMRegisterLabel {
    R0, R1, R2, R3, R4, R5, R6, R7, R8, R9, R10, R11, R12, SP, SLR, PC
}

/* class for concrete representation of registerse, as opposed to PseudoRegister class */
public class ARMConcreteRegister {

    public static final int MAX_ARM_REGISTER = 16;
    private ARMRegisterLabel label;

    public ARMConcreteRegister(ARMRegisterLabel label) {
        this.label = label;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ARMConcreteRegister) {
            ARMConcreteRegister that = (ARMConcreteRegister) obj;
            return this.label.equals(that.label);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return label.hashCode();
    }

    @Override
    public String toString() {
        return label.name();
    }
}

