package backend.intel.instructions;

import backend.common.MovInstruction;
import backend.common.address.Address;
import java.util.Locale;
import utils.Utils;
import utils.backend.register.Register;
import utils.backend.register.intel.IntelConcreteRegister;

public class Mov extends MovInstruction implements IntelInstruction {

  public enum IntelMovType {
    MOV, MOVZBQ, MOVZLQ
  }

  private final IntelMovType type;

  public Mov(Register rs, Register rd, Address operand2) {
    super(rs, rd, operand2);
    this.type = IntelMovType.MOV;
  }

  public Mov(Address operand2, Register Rd, IntelMovType type) {
    super(Rd, operand2);
    this.type = type;
  }

  public Mov(Address operand2, Register Rd) {
    this(operand2, Rd, IntelMovType.MOV);
  }

  public Mov(Register Rs, Register Rd) {
    super(Rs, Rd);
    this.type = IntelMovType.MOV;
  }

  public Mov(Register Rs, Address operand2) {
    super(operand2, Rs);
    this.type = IntelMovType.MOV;
  }

  @Override
  public String assemble() {
    StringBuilder str = new StringBuilder();

    boolean isNormalMov = type.equals(IntelMovType.MOV);

    if (!isNormalMov) {
      str.append(type.name().toLowerCase(Locale.ROOT));
    }

    String size = "";
    StringBuilder everythingAfter = new StringBuilder();

    if (operand2 == null) {
      size = Utils.calculateSize(rs.asIntelRegister().getSize());
      everythingAfter.append(rs).append(", ").append(rd);
    } else if (rs == null) {
      size = Utils.calculateSize(rd.asIntelRegister().getSize());
      everythingAfter.append(operand2).append(", ").append(rd);
    } else if (rd == null) {
      size = Utils.calculateSize(rs.asIntelRegister().getSize());
      everythingAfter.append(rs).append(", ").append(operand2);
    }

    return str.append(isNormalMov ? "mov" + size : "").append(" ").append(everythingAfter.toString()).toString();
  }
}
