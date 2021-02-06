package Node.Stat;

import Node.Node;

import java.util.List;

/**
 * describe a single statement */
public interface StatNode {

  // what is the use of this?
  default boolean isSeq() {
    return false;
  }
  
  // does the statement(s) include return or exit statement 
  default boolean hasEnd() { return false; }

  // does the statement(s) has return statement
  default boolean isReturn() { return false; }

}
