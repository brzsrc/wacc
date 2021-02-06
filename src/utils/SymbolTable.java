package utils;

import java.util.HashMap;
import Node.Node;
import Node.Expr.ExprNode;

public class SymbolTable {

    private HashMap<String, ExprNode> dictionary;
    private SymbolTable parentSymbolTable;

    public SymbolTable() {
        this.dictionary = new HashMap<String, ExprNode>();
        this.parentSymbolTable = null;
    }

    public void add(String name, ExprNode node) {
        if (dictionary.containsKey(name)) {
            throw new IllegalArgumentException("redefinition of ident: " + name + " is not allowed");
        }
        this.dictionary.put(name, node);
    }

    public ExprNode lookup(String name) {
        return dictionary.get(name);
    }

    public Node lookupAll(String name) {
        SymbolTable table = this;
        Node obj = null;
        while(obj == null && table != null) {
            obj = table.dictionary.get(name);
            table = table.parentSymbolTable;
        }
        return obj;
    }
}