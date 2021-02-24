package utils.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static utils.backend.ARMConcreteRegister.*;

public class ARMConcreteRegisterAllocator {

    public static final int GENERAL_REG_START = 4;
    public static final Map<ARMRegisterLabel, Integer> ARMspecialRegMapping = Map.of(ARMRegisterLabel.SP, 13,
            ARMRegisterLabel.SLR, 14, ARMRegisterLabel.PC, 15);

    private List<ARMConcreteRegister> registers;
    private int registerCounter;

    public ARMConcreteRegisterAllocator() {
        registers = new ArrayList<>();
        Arrays.asList(ARMRegisterLabel.values()).forEach(label -> registers.add(new ARMConcreteRegister(label)));
        registerCounter = GENERAL_REG_START;
    }

    public ARMConcreteRegister next() {
        return isFull() ? null : registers.get(registerCounter + 1);
    }

    public ARMConcreteRegister get(int counter) {
        return registers.get(counter);
    }

    public ARMConcreteRegister get(ARMRegisterLabel label) {
        return registers.get(ARMspecialRegMapping.get(label));
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
