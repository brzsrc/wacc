package utils.frontend.symbolTable;

import frontend.node.expr.ExprNode;

public class Symbol {
    private ExprNode node;
    private int stackOffset;
  
    public Symbol(ExprNode node, int stackOffset) {
      this.node = node;
      this.stackOffset = stackOffset;
    }

    /** IMPORTANT 
     *   1 only used when update params 
     *   2 since operant store offset from top of scope, push means move up stack offset 
     *     should decrease offset  
     */
    public void pushStackOffset(int stackOffset) {
      // System.out.println("before stackOffset is " + this.stackOffset);
      // System.out.println("after stackOffset is " + (this.stackOffset - stackOffset));
      this.stackOffset -= stackOffset;
    }
  
    public ExprNode getExprNode() {
      return node;
    }
  
    public int getStackOffset() {
      return stackOffset;
    }
  }
