package utils.frontend.symbolTable;

import java.util.HashMap;

import frontend.node.expr.ExprNode;
import utils.frontend.SemanticErrorHandler;

public class SymbolTable {

  /**
   * SymbolTable will record an identifier String and an ExprNode as the node representing the value
   * of that identifier in the current scope. It will also contain a copy of its parent SymbolTable.
   * The parent of the root SymbolTable will be set to null.
   */

  private final HashMap<String, Symbol> dictionary;
  private final SymbolTable parentSymbolTable;

  public SymbolTable(SymbolTable parentSymbolTable) {
    this.dictionary = new HashMap<>();
    this.parentSymbolTable = parentSymbolTable;
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

  public Symbol lookup(String name) {
    return dictionary.get(name);
  }

  public Symbol lookupAll(String name) {
    SymbolTable st = this;
    Symbol obj = null;
    while (obj == null && st != null) {
      obj = st.lookup(name);
      st = st.parentSymbolTable;
    }

    return obj;
  }

  public int getSize() {
    return dictionary.size();
  }

  public SymbolTable getParentSymbolTable() {
    return parentSymbolTable;
  }

  public int getStackOffset(String ident) {
    return dictionary.get(ident).getStackOffset();
  }
}
