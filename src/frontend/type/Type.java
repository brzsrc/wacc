package frontend.type;

public interface Type {

  boolean equalToType(Type other);

  default PairType asPairType() {
    throw new IllegalArgumentException("cast not allowed");
  }

  default ArrayType asArrayType() {
    throw new IllegalArgumentException("cast not allowed");
  }

  void showType();
}