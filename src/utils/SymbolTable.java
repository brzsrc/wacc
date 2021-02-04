package utils;

import java.util.HashMap;
import utils.Type.Type;

public class SymbolTable {
    private HashMap<String, Type> dictionary;
    private SymbolTable encSymTable;

    public SymbolTable(SymbolTable encSymTable) {
        this.encSymTable = encSymTable;
        this.dictionary = new HashMap<>();
    }

    public void add(String name, Type type) {
        dictionary.put(name, type);
    }

    public Type lookUp(String name) {
        return dictionary.get(name);
    }

    public Type lookUpAll(String name) {
        SymbolTable table = this;
        Type obj = null;
        while(obj == null && table != null) {
            obj = table.dictionary.get(name);
            table = table.encSymTable;
        }
        return obj;
    }
}