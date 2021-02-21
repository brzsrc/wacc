package utils.backend;

/* represent a pseudo register with infinite amount of register supplies, with labeling from T0 to Tn */
public class PseudoRegister extends Register<String> {

    public PseudoRegister(int num) {
        this.label = "T" + num;
    }

}
