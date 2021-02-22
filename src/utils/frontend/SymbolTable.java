package utils.frontend;

import java.util.HashMap;

import frontend.node.expr.ExprNode;

public class SymbolTable {

  /**
   * SymbolTable will record an identifier String and an ExprNode as the node representing the value
   * of that identifier in the current scope. It will also contain a copy of its parent SymbolTable.
   * The parent of the root SymbolTable will be set to null.
   */

  private final HashMap<String, ExprNode> dictionary;
  private final SymbolTable parentSymbolTable;

  public SymbolTable(SymbolTable parentSymbolTable) {
    this.dictionary = new HashMap<>();
    this.parentSymbolTable = parentSymbolTable;
  }

  public boolean add(String name, ExprNode expr) {
    if (dictionary.containsKey(name)) {
      SemanticErrorHandler.symbolRedeclared(null, name);
      return true;
    }
    this.dictionary.put(name, expr);
    return false;
  }

  public ExprNode lookup(String name) {
    return dictionary.get(name);
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

}
