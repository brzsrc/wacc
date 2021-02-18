package backend.utils;

import java.util.HashMap;
import java.util.HashSet;

public class RegisterAllocator {
    public Register r0;
    public Register r1; 
    public Register r2; 
    public Register r3; 
    public Register r4; 
    public Register r5; 
    public Register r6;  
    public Register r7;  
    public Register r8;  
    public Register r9;  
    public Register r10; 
    public Register r11; 
    public Register r12; 
    public Register sp;  //r13
    public Register lr;  //r14
    public Register pc;  //r15
    public Register cpsr;  //r16

    private HashMap<Integer, Register> allRegs;
    private HashSet regsInUse;
    private Register nextAvailableReg;

    public RegisterAllocator() {
        allRegs = new HashMap<>();
        regsInUse = new HashSet<>();
        for(int i = 0; i < 17; i++) {
            allRegs.put(i, new Register(i));
        }

        this.r0 = allRegs.get(0);
        this.r1 = allRegs.get(1);
        this.r2 = allRegs.get(2);
        this.r3 = allRegs.get(3);
        this.r4 = allRegs.get(4);
        this.r5 = allRegs.get(5);
        this.r6 = allRegs.get(6);
        this.r7 = allRegs.get(7);
        this.r8 = allRegs.get(8);
        this.r9 = allRegs.get(9);
        this.r10 = allRegs.get(10);
        this.r11 = allRegs.get(11);
        this.r12= allRegs.get(12);
        this.sp = allRegs.get(13);
        this.lr = allRegs.get(14);
        this.pc = allRegs.get(15);
        this.cpsr = allRegs.get(16);
        nextAvailableReg = this.r0;
    }

    public boolean isFull() {
        return regsInUse.size() == 13;
    }

    public Register nextAvailable() {
        return nextAvailableReg;
    }

    public boolean isInUse(Register reg) {
        return regsInUse.contains(reg);
    }

    public boolean setInUse(Register reg) {
        return regsInUse.add(reg);
    }

    public boolean free(Register reg) {
        return regsInUse.remove(reg);
    }

    public void allocate() {

    }

}
