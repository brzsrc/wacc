package Node;

import java.util.List;

/**
 * describe a single statement */
public interface StatNode {

  // what is the use of this?
  default boolean isSeq() {
    return false;
  }

  default boolean hasEnd() { return false; }

  //main function could not have return, maybe useful for checking this
  default boolean isReturn() { return false; }

}
