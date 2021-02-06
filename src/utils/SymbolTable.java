package utils;

import java.util.HashMap;
import java.util.List;
import Node.Node;

import Type.Type;

public class SymbolTable {

    private HashMap<String, Node> dictionary;
    private SymbolTable parentSymbolTable;

    public SymbolTable() {
        this.dictionary = new HashMap<String, Node>();
        this.parentSymbolTable = null;
    }

    public void add(String name, Node node) {
        if (dictionary.containsKey(name)) {
            throw new IllegalArgumentException("redefinition of ident: " + name + " is not allowed");
        }
        this.dictionary.put(name, node);
    }

    public Node lookup(String name) {
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