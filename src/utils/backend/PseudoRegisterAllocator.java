package utils.backend;

public class PseudoRegisterAllocator {
    
    private int counter;

    public PseudoRegisterAllocator() {
        counter = 0;
    }

    public PseudoRegister get() {
        return new PseudoRegister(counter);
    }

    public PseudoRegister next() {
        return new PseudoRegister(counter + 1);
    }

    public PseudoRegister allocate() {
        return new PseudoRegister(counter++);
    }

    public PseudoRegister free() {
        return new PseudoRegister(counter--);
    }
}
