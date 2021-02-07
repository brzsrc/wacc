package Node.Stat;

import Node.Node;

import java.util.List;

/**
 * describe a single statement */
public interface StatNode {
  
  /**
   * does the statement(s) end with return or exit statement
   * usage: function should end with statement hasEnd() but NO OTHER STATEMENT AFTER
   *        main scope no restriction */
  default boolean hasEnd() { return false; }

  /** does the statement(s) include return statement
   * usage: function no restriction
   *       main scope should not allow return instruction */
  default boolean hasReturn() { return false; }

}
