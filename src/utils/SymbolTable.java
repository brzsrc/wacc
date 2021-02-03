package utils;

import java.util.HashMap;
import antlr.WACCParser;

public class SymbolTable {
    private HashMap<String, TypeSystem> dictionary;
    private SymbolTable encSymTable;

    public SymbolTable(SymbolTable encSymTable) {
        this.encSymTable = encSymTable;
        this.dictionary = new HashMap<>();
    }

    public void add(String name, TypeSystem type) {
        dictionary.put(name, type);
    }

    public TypeSystem lookUp(String name) {
        return dictionary.get(name);
    }

    public TypeSystem lookUpAll(String name) {
        SymbolTable table = this;
        TypeSystem obj = null;
        while(obj == null && table != null) {
            obj = table.dictionary.get(name);
            table = table.encSymTable;
        }
        return obj;
    }
}