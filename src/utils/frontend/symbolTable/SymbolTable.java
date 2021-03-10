package utils.frontend.symbolTable;

import java.util.HashMap;

import frontend.node.expr.ExprNode;
import frontend.node.expr.IdentNode;
import utils.frontend.SemanticErrorHandler;

public class SymbolTable {

  /**
   * SymbolTable will record an identifier String and an ExprNode as the node representing the value
   * of that identifier in the current scope. It will also contain a copy of its parent SymbolTable.
   * The parent of the root SymbolTable will be set to null.
   */

  private final HashMap<String, Symbol> dictionary;
  private final SymbolTable parentSymbolTable;
  private int scopeSize;

  public SymbolTable(SymbolTable parentSymbolTable) {
    this.dictionary = new HashMap<>();
    this.parentSymbolTable = parentSymbolTable;
    scopeSize = 0;
  }

  public boolean add(String name, ExprNode expr, int stackOffset) {
    if (dictionary.containsKey(name)) {
      SemanticErrorHandler.symbolRedeclared(null, name);
      return true;
    }
    
    this.dictionary.put(name, new Symbol(expr, stackOffset));
    scopeSize += expr.getType().getSize();
    return false;
  }

  public boolean add(String name, ExprNode expr) {
    if (dictionary.containsKey(name)) {
      SemanticErrorHandler.symbolRedeclared(null, name);
      return true;
    }
    
    scopeSize += expr.getType().getSize();
    this.dictionary.put(name, new Symbol(expr, scopeSize));
    
    return false;
  }

  public Symbol lookup(String name) {
    // System.out.println("looking up " + name + dictionary.get(name).getStackOffset());
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
    return scopeSize;
  }

  public SymbolTable getParentSymbolTable() {
    return parentSymbolTable;
  }

  public int getStackOffset(String name, Symbol symbol) {
    /** change from previous implementation:
     *    Offset in map is from top of stack to bottom
     *        +-----------+
     * scope1 | val1 | +6 |
     *        | val2 | +4 |
     *        | val3 | +0 |
     * scope2 | val4 | +4 |
     *        | val5 | +0 |
     *        +-----------+
     */
    /* if ident is defined in current scope, return its offset */
    if (dictionary.containsKey(name)
            && dictionary.get(name) == symbol) {
      return symbol.getStackOffset();
    }
    /* else, get its offset from upper scope */
    if (parentSymbolTable != null) {
      return parentSymbolTable.getStackOffset(name, symbol) - parentSymbolTable.getSize();
    }

    /* else, unhandled ident undefined error from semantic checker */
    throw new IllegalArgumentException("should have handled undefined ident error in semantic checker");
  }
}
