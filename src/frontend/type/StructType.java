package frontend.type;

import utils.Utils;
import utils.Utils.AssemblyArchitecture;

public class StructType implements Type {

  private final String name;
  private final AssemblyArchitecture arch;

  public StructType(String name, AssemblyArchitecture arch) {
    this.name = name;
    this.arch = arch;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equalToType(Type other) {
    if (other == null) {
      return true;
    }

    if (!(other instanceof StructType)) {
      return false;
    }

    StructType otherStruct = (StructType) other;

    if (name == null || otherStruct.name == null) {
      return true;
    }

    return name.equals(otherStruct.getName());
  }

  @Override
  public void showType() {
    System.out.print(this.toString());
  }

  @Override
  public int getSize() {
    return arch.equals(AssemblyArchitecture.ARMv6) ? Utils.ARM_POINTER_SIZE : Utils.INTEL_POINTER_SIZE;
  }

  @Override
  public String toString() {
    return (name != null) ? name : "empty";
  }
}
