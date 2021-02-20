package frontend.type;

public interface Type {
  /* word, byte size in unit: byte */
  int WORD_SIZE = 4, BYTE_SIZE = 1;

  boolean equalToType(Type other);

  default PairType asPairType() {
    throw new IllegalArgumentException("cast not allowed");
  }

  default ArrayType asArrayType() {
    throw new IllegalArgumentException("cast not allowed");
  }

  void showType();

  int getSize();
}