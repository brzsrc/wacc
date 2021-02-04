package node;

public interface StatNode extends Node {

  default boolean isSeq() {
    return false;
  }

  default boolean hasEnd() { return false; }

  //main function could not have return, maybe useful for checking this
  default boolean isReturn() { return false; }

}
