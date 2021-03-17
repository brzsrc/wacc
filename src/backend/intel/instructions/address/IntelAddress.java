package backend.intel.instructions.address;

import backend.common.address.Address;
import backend.common.address.Immediate;
import backend.intel.instructions.Label;
import utils.Utils.IntelInstructionSize;
import utils.backend.register.intel.IntelConcreteRegister;

public class IntelAddress extends Address {

  /* represent the immediate addresesing, i.e. movl $7, %eax */
  private final IntelImmediate immed;
  /* the base register in every form of addressing */
  private final IntelConcreteRegister Rb;
  /* the constant displacement in front of Rb, i.e. movl 0x100(%rbp), %eax */
  private final IntelImmediate displacement;
  /* the index register used in index addressing */
  private final IntelConcreteRegister Ri;
  /* scale used in index addressing */
  private final int scale;

  /* base constructor of this class */
  private IntelAddress(IntelImmediate immed, IntelConcreteRegister rb, IntelImmediate displacement,
      IntelConcreteRegister ri, int scale) {
    this.immed = immed;
    Rb = rb;
    this.displacement = displacement;
    Ri = ri;
    this.scale = scale;
  }

  /* This represents the Immediate int value addressing, i.e. movl $7, %eax */
  public IntelAddress(int val) {
    this(new IntelImmediate(val, IntelInstructionSize.L), null, null, null, 0);
  }

  /* This represents normal addressing, i.e. movq (%rbp), %rax*/
  public IntelAddress(IntelConcreteRegister Rb) {
    this(null, Rb, null, null, 0);
  }

  /* This represents the displacement addressing, i.e. movl 8(%rdi), %eax */
  public IntelAddress(IntelConcreteRegister rb, int displacement) {
    this(null, rb, new IntelImmediate(displacement, IntelInstructionSize.L), null, 0);
  }

  public IntelAddress(IntelConcreteRegister rb, Label label) {
    this(null, rb, new IntelImmediate(label), null, 0);
  }

  /* This represents the indexed addressing */
  public IntelAddress(IntelConcreteRegister rb, int displacement,
      IntelConcreteRegister ri, int scale) {
    this(null, rb, new IntelImmediate(displacement, IntelInstructionSize.L), ri, scale);
  }

  @Override
  public String toString() {
    if (immed != null) {
      return immed.assemble();
    }

    StringBuilder str = new StringBuilder();
    str.append(displacement == null ? "" : displacement.assemble().substring(1));
    str.append("(")
        .append(Rb == null ? "" : Rb)
        .append(Ri == null ? "" : ", " + Ri)
        .append(scale == 0 ? "" : ", " + scale)
        .append(")");
    return str.toString();
  }

}
