package frontend.type;

import static utils.Utils.*;

import utils.Utils.AssemblyArchitecture;

public class PairType implements Type {

  private final int PAIR_HASH_CODE = 10;
  private final Type fstType;
  private final Type sndType;
  private final AssemblyArchitecture arch;

  public PairType(Type fstType, Type sndType, AssemblyArchitecture arch) {
    this.fstType = fstType;
    this.sndType = sndType;
    this.arch = arch;
  }

  public PairType(AssemblyArchitecture arch) {
    this(null, null, arch);
  }

  public Type getFstType() {
    return fstType;
  }

  public Type getSndType() {
    return sndType;
  }

  @Override
  public PairType asPairType() {
    return this;
  }

  @Override
  public boolean equalToType(Type other) {
    if (other == null) {
      return true;
    }
    if (!(other instanceof PairType)) {
      return false;
    }

    PairType otherPair = (PairType) other;

    return subTypeCoerce(fstType, otherPair.fstType)
        && subTypeCoerce(sndType, otherPair.sndType);
  }

  private boolean subTypeCoerce(Type thisType, Type thatType) {
    if (thisType == null || thatType == null) {
      /* if either thisType or thatType is null, then we can coerce them
       * see comments in PairNode class for more information */
      return true;
    } else if (thisType instanceof PairType) {
      /*  */
      return thatType instanceof PairType;
    }

    return thisType.equalToType(thatType);
  }

  @Override
  public String toString() {
    if (fstType == null || sndType == null) {
      return "null";
    }
    String fst = (fstType instanceof PairType) ? "pair" : fstType.toString();
    String snd = (sndType instanceof PairType) ? "pair" : sndType.toString();
    return "pair(" + fst + ", " + snd + ")";
  }

  @Override
  public void showType() {
    System.out.print("pair<");
    showChild(fstType);
    System.out.print(", ");
    showChild(sndType);
    System.out.print(">");
  }

  private void showChild(Type child) {
    if (child == null) {
      System.out.print("null");
    } else {
      child.showType();
    }
  }

  @Override
  public int getSize() {
    return arch.equals(AssemblyArchitecture.ARMv6) ? ARM_POINTER_SIZE : INTEL_POINTER_SIZE;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Type) {
      return this.equalToType((Type) obj);
    }

    return false;
  }

  @Override
  public int hashCode() {
    return PAIR_HASH_CODE;
  }
}
