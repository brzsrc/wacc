package backend.intel.instructions;

import backend.common.MovInstruction;
import backend.common.address.Address;
import utils.Utils;
import utils.backend.register.Register;
import utils.backend.register.intel.IntelConcreteRegister;

public class Mov extends MovInstruction implements IntelInstruction {

  public Mov(Register rs, Register rd, Address operand2) {
    super(rs, rd, operand2);
  }

  public Mov(Address operand2, Register Rd) {
    super(Rd, operand2);
  }

  public Mov(Register Rs, Register Rd) {
    super(Rs, Rd);
  }

  public Mov(Register Rs, Address operand2) {
    super(operand2, Rs);
  }

  @Override
  public String assemble() {
    StringBuilder str = new StringBuilder();

    str.append("mov");

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

    return str.append(size).append(" ").append(everythingAfter.toString()).toString();
  }
}
