package utils.backend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static utils.backend.ARMConcreteRegister.*;

public class ARMConcreteRegisterAllocator {

    public static final int GENERAL_REG_START = 4, GENERAL_REG_END = 12;
    public static final Map<ARMRegisterLabel, Integer> ARMspecialRegMapping = Map.of(
            ARMRegisterLabel.R0, 0,
            ARMRegisterLabel.SP, 13,
            ARMRegisterLabel.LR, 14,
            ARMRegisterLabel.PC, 15);

    private List<ARMConcreteRegister> registers;
    private int registerCounter;

    public ARMConcreteRegisterAllocator() {
        registers = new ArrayList<>();
        Arrays.asList(ARMRegisterLabel.values()).forEach(label -> registers.add(new ARMConcreteRegister(label)));
        registerCounter = GENERAL_REG_START;
    }

    public ARMConcreteRegister curr() {
        return registers.get(registerCounter > GENERAL_REG_START ? registerCounter - 1 : registerCounter);
    }

    public ARMConcreteRegister last() {
        return registers.get(registerCounter > GENERAL_REG_START ? registerCounter - 2 : registerCounter);
    }

    public ARMConcreteRegister next() {
        return isFull() ? null : registers.get(registerCounter + 1);
    }

    public ARMConcreteRegister get(int counter) {

        return registers.get(counter);
    }

    public ARMConcreteRegister get(ARMRegisterLabel label) {
        return get(ARMspecialRegMapping.get(label));
    }

    public ARMConcreteRegister allocate() {
        if (registerCounter < GENERAL_REG_START || registerCounter > GENERAL_REG_END) {
            throw new IllegalArgumentException("cannot allocate register number: " + registerCounter);
        }
        return registers.get(registerCounter++);
    }

    public ARMConcreteRegister free() {
        System.out.println("freed");
        return registers.get(--registerCounter);
    }

    private boolean isFull() {
        return registerCounter < MAX_ARM_REGISTER;
    }

}
