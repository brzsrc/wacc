package utils.backend.register.arm;

import utils.backend.register.Register;

/* class for concrete representation of registerse, as opposed to PseudoRegister class */
public class ARMConcreteRegister extends Register {

  /* static ARM register references */
  public static final ARMConcreteRegister r0 = new ARMConcreteRegister(ARMRegisterLabel.R0);
  public static final ARMConcreteRegister r1 = new ARMConcreteRegister(ARMRegisterLabel.R1);
  public static final ARMConcreteRegister r2 = new ARMConcreteRegister(ARMRegisterLabel.R2);
  public static final ARMConcreteRegister r3 = new ARMConcreteRegister(ARMRegisterLabel.R3);
  public static final ARMConcreteRegister r4 = new ARMConcreteRegister(ARMRegisterLabel.R4);
  public static final ARMConcreteRegister r5 = new ARMConcreteRegister(ARMRegisterLabel.R5);
  public static final ARMConcreteRegister r6 = new ARMConcreteRegister(ARMRegisterLabel.R6);
  public static final ARMConcreteRegister r7 = new ARMConcreteRegister(ARMRegisterLabel.R7);
  public static final ARMConcreteRegister r8 = new ARMConcreteRegister(ARMRegisterLabel.R8);
  public static final ARMConcreteRegister r9 = new ARMConcreteRegister(ARMRegisterLabel.R9);
  public static final ARMConcreteRegister r10 = new ARMConcreteRegister(ARMRegisterLabel.R10);
  public static final ARMConcreteRegister r11 = new ARMConcreteRegister(ARMRegisterLabel.R11);
  public static final ARMConcreteRegister r12 = new ARMConcreteRegister(ARMRegisterLabel.R12);
  public static final ARMConcreteRegister LR = new ARMConcreteRegister(ARMRegisterLabel.LR);
  public static final ARMConcreteRegister PC = new ARMConcreteRegister(ARMRegisterLabel.PC);
  public static final ARMConcreteRegister SP = new ARMConcreteRegister(ARMRegisterLabel.SP);

  public static final int MAX_ARM_REGISTER = 16;

  private final ARMRegisterLabel label;

  public ARMConcreteRegister(ARMRegisterLabel label) {
    this.label = label;
  }

  @Override
  public String toString() {
    return label.toString().toLowerCase();
  }
}

