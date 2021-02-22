package utils.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static utils.backend.ARMConcreteRegister.*;

public class ARMConcreteRegisterAllocator {

    private List<ARMConcreteRegister> registers;
    private int registerCounter;

    public ARMConcreteRegisterAllocator() {
        registers = new ArrayList<>();
        Arrays.asList(ARMRegisterLabel.values()).forEach(label -> registers.add(new ARMConcreteRegister(label)));
        registerCounter = 0;
    }

    public ARMConcreteRegister next() {
        return isFull() ? null : registers.get(registerCounter + 1);
    }

    public ARMConcreteRegister get(int counter) {
        return registers.get(counter);
    }

    public ARMConcreteRegister allocate() {
        return registers.get(registerCounter++);
    }

    public ARMConcreteRegister free() {
        return registers.get(registerCounter--);
    }

    private boolean isFull() {
        return registerCounter < MAX_ARM_REGISTER;
    }

}
