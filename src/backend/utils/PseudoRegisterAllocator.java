package backend.utils;

public class PseudoRegisterAllocator {
    
    private int counter;

    public PseudoRegisterAllocator() {
        counter = 0;
    }

    public int get() {
        return counter;
    }

    public int next() {
        return counter + 1;
    }

    public int allocate() {
        return counter++;
    }

    public int free() {
        return counter--;
    }
}
