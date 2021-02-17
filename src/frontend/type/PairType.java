package frontend.type;

import frontend.node.expr.ExprNode;

public class PairType implements Type {

  private final Type fstType;
  private final Type sndType;

  public PairType(Type fstType, Type sndType) {
    this.fstType = fstType;
    this.sndType = sndType;
  }

  public PairType() {
    this(null, null);
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
    return "Pair<" + fstType + ", " + sndType + ">";
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
}
