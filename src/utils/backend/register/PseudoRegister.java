package utils.backend.register;

/* represent a pseudo register with infinite amount of register supplies, with labeling from T0 to Tn */
public class PseudoRegister extends Register {

    private String label;

    public PseudoRegister(int num) {
        this.label = "T" + num;
    }

}
