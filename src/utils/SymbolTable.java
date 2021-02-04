package utils;

import java.util.HashMap;
import antlr.WACCParser;

public class SymbolTable {
    private HashMap<String, WACCType> dictionary;
    private SymbolTable encSymTable;

    public SymbolTable(SymbolTable encSymTable) {
        this.encSymTable = encSymTable;
        this.dictionary = new HashMap<>();
    }

    public void add(String name, WACCType type) {
        dictionary.put(name, type);
    }

    public WACCType lookUp(String name) {
        return dictionary.get(name);
    }

    public WACCType lookUpAll(String name) {
        SymbolTable table = this;
        WACCType obj = null;
        while(obj == null && table != null) {
            obj = table.dictionary.get(name);
            table = table.encSymTable;
        }
        return obj;
    }
}