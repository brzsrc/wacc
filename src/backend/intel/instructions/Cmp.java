package backend.intel.instructions;

import backend.common.CmpInstruction;
import backend.common.address.Address;
import backend.intel.instructions.address.IntelAddress;
import utils.Utils;
import utils.backend.register.Register;
import utils.backend.register.intel.IntelConcreteRegister;

public class Cmp extends CmpInstruction implements IntelInstruction {

  private final IntelAddress addr;

  public Cmp(Register rs, Register rd) {
    super(rd, rs);
    this.addr = null;
  }

  public Cmp(Register rs, IntelAddress addr) {
    super(rs, addr);
    this.addr = addr;
  }

  @Override
  public String assemble() {
    StringBuilder str = new StringBuilder();
    str.append("cmp");
    if (addr == null) {
      str.append(Utils.calculateSize(rs.asIntelRegister().getSize(), rd.asIntelRegister()
          .getSize())).append(" ");
    } else {
      str.append(Utils.calculateSize(rd.asIntelRegister().getSize())).append(" ");
    }

    str.append(rs + ", ");
    str.append(rd);
    str.append(addr == null ? "" : addr);

    return str.toString();
  }
}
