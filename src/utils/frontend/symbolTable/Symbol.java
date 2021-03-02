package utils.frontend.symbolTable;

import frontend.node.expr.ExprNode;

public class Symbol {
    private ExprNode node;
    private int stackOffset;
  
    public Symbol(ExprNode node, int stackOffset) {
      this.node = node;
      this.stackOffset = stackOffset;
    }

    public void pushStackOffset(int stackOffset) {
      this.stackOffset += stackOffset;
    }
  
    public ExprNode getExprNode() {
      return node;
    }
  
    public int getStackOffset() {
      return stackOffset;
    }
  }
