package utils.backend.register.intel;

public class IntelConcreteRegister {
  public static final int MAX_INTEL_REGISTER = 16;

  public static final IntelConcreteRegister rax = new IntelConcreteRegister(IntelRegisterLabel.RAX);
  public static final IntelConcreteRegister rbx = new IntelConcreteRegister(IntelRegisterLabel.RBX);
  public static final IntelConcreteRegister rcx = new IntelConcreteRegister(IntelRegisterLabel.RCX);
  public static final IntelConcreteRegister rdx = new IntelConcreteRegister(IntelRegisterLabel.RDX);
  public static final IntelConcreteRegister rsi = new IntelConcreteRegister(IntelRegisterLabel.RSI);
  public static final IntelConcreteRegister rdi = new IntelConcreteRegister(IntelRegisterLabel.RDI);
  public static final IntelConcreteRegister rbp = new IntelConcreteRegister(IntelRegisterLabel.RBP);
  public static final IntelConcreteRegister rsp = new IntelConcreteRegister(IntelRegisterLabel.RSP);
  public static final IntelConcreteRegister r8 = new IntelConcreteRegister(IntelRegisterLabel.R8);
  public static final IntelConcreteRegister r9 = new IntelConcreteRegister(IntelRegisterLabel.R9);
  public static final IntelConcreteRegister r10 = new IntelConcreteRegister(IntelRegisterLabel.R10);
  public static final IntelConcreteRegister r11 = new IntelConcreteRegister(IntelRegisterLabel.R11);
  public static final IntelConcreteRegister r12 = new IntelConcreteRegister(IntelRegisterLabel.R12);
  public static final IntelConcreteRegister r13 = new IntelConcreteRegister(IntelRegisterLabel.R13);
  public static final IntelConcreteRegister r14 = new IntelConcreteRegister(IntelRegisterLabel.R14);
  public static final IntelConcreteRegister r15 = new IntelConcreteRegister(IntelRegisterLabel.R15);
  public static final IntelConcreteRegister rip = new IntelConcreteRegister(IntelRegisterLabel.RIP);

  private final IntelRegisterLabel label;

  public IntelConcreteRegister(IntelRegisterLabel label) {
    this.label = label;
  }

  @Override
  public String toString() {
    return "%" + label.name().toLowerCase();
  }
}
