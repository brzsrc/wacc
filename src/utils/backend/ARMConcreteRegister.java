package utils.backend;

/* class for concrete representation of registerse, as opposed to PseudoRegister class */
public class ARMConcreteRegister extends Register<ARMRegisterLabel> {

    public static final int MAX_ARM_REGISTER = 16;

    public ARMConcreteRegister(ARMRegisterLabel label) {
        this.label = label;
    }
}

