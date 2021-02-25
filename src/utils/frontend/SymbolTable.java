package utils.frontend;

import java.util.HashMap;

import frontend.node.expr.ExprNode;

class Symbol {
  private ExprNode node;
  private int stackOffset;

  public Symbol(ExprNode node, int stackOffset) {
    this.node = node;
    this.stackOffset = stackOffset;
  }

  public ExprNode getExprNode() {
    return node;
  }

  public void setNode(ExprNode node) {
    this.node = node;
  }

  public int getStackOffset() {
    return stackOffset;
  }

  public void setStackOffset(int stackOffset) {
    this.stackOffset = stackOffset;
  }
}

public class SymbolTable {

  /**
   * SymbolTable will record an identifier String and an ExprNode as the node representing the value
   * of that identifier in the current scope. It will also contain a copy of its parent SymbolTable.
   * The parent of the root SymbolTable will be set to null.
   */

  private final HashMap<String, Symbol> dictionary;
  private final SymbolTable parentSymbolTable;
  private int stackOffsetCounter;

  public SymbolTable(SymbolTable parentSymbolTable) {
    this.dictionary = new HashMap<>();
    this.parentSymbolTable = parentSymbolTable;
    this.stackOffsetCounter = 0;
  }

  public boolean add(String name, ExprNode expr, int stackOffset) {
    if (dictionary.containsKey(name)) {
      SemanticErrorHandler.symbolRedeclared(null, name);
      return true;
    }
    
    this.dictionary.put(name, new Symbol(expr, stackOffset));
    stackOffset += expr.getType().getSize();
    return false;
  }

  public ExprNode lookup(String name) {
    return dictionary.get(name).getExprNode();
  }

  public ExprNode lookupAll(String name) {
    SymbolTable st = this;
    ExprNode obj = null;
    while (obj == null && st != null) {
      obj = st.lookup(name);
      st = st.parentSymbolTable;
    }

    return obj;
  }

  public SymbolTable getParentSymbolTable() {
    return parentSymbolTable;
  }

  public int getStackOffset(String ident) {
    return dictionary.get(ident).getStackOffset();
  }
}
