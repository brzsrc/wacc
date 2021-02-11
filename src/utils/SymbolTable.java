package utils;

import java.util.HashMap;

import node.expr.ExprNode;

public class SymbolTable {

    // symbol table only record declaired type, so should map type instead of node?
    // node is determinded at run time, i.e x can be assigned 1 + 2 later, symbol table should't change on that
    private HashMap<String, ExprNode> dictionary;
    private SymbolTable parentSymbolTable;

    public SymbolTable(SymbolTable parentSymbolTable) {
        this.dictionary = new HashMap<>();
        this.parentSymbolTable = parentSymbolTable;
    }

    public void add(String name, ExprNode expr) {
        if (dictionary.containsKey(name)) {
            SemanticErrorHandler.symbolRedeclared(null, name);
        }
        this.dictionary.put(name, expr);
    }

    public HashMap<String, ExprNode> getDictionary() {
        return dictionary;
    }

    public ExprNode lookup(String name) {
        return dictionary.get(name);
    }

    public ExprNode lookupAll(String name) {
        SymbolTable table = this;
        ExprNode obj = null;
        while(obj == null && table != null) {
            obj = table.dictionary.get(name);
            table = table.parentSymbolTable;
        }
        
        return obj;
    }

    public SymbolTable getParentSymbolTable() {
        return parentSymbolTable;
    }

}
