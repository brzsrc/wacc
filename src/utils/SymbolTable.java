package utils;

import java.util.HashMap;
import type.Type;

public class SymbolTable {

    // symbol table only record declaired type, so should map type instead of node?
    // node is determinded at run time, i.e x can be assigned 1 + 2 later, symbol table should't change on that
    private HashMap<String, Type> dictionary;
    private SymbolTable parentSymbolTable;

    public SymbolTable() {
        this.dictionary = new HashMap<>();
        this.parentSymbolTable = null;
    }

    public void add(String name, Type type) {
        if (dictionary.containsKey(name)) {
            // todo: change to using ErrorHandler
            throw new IllegalArgumentException("redefinition of ident: " + name + " is not allowed");
        }
        this.dictionary.put(name, type);
    }

    public HashMap<String, Type> getDictionary() {
        return dictionary;
    }

    public Type lookup(String name) {
        return dictionary.get(name);
    }

    public Type lookupAll(String name) {
        SymbolTable table = this;
        Type obj = null;
        while(obj == null && table != null) {
            obj = table.dictionary.get(name);
            table = table.parentSymbolTable;
        }
        return obj;
    }

    // create a symbol table for this scope, set up parent field of the returned symbol table
    public SymbolTable createScope() {
        SymbolTable result = new SymbolTable();
        result.parentSymbolTable = this;
        return result;
    }

}