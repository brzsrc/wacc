package utils.IR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utils.Type.Type;

public class SymbolTable {
    private SymbolTableScope root;
    private SymbolTableScope currentSymbolTable;
    // maybe can add a map, map SymbolTableScope with ScopeNode?

    public SymbolTable() {
        this.root = new SymbolTableScope(null);
        this.currentSymbolTable = root;
    }

    // add a ident type map in current scope
    public void add(String name, Type type) {
        currentSymbolTable.add(name, type);
    }

    // lookup type of ident in current scope
    public Type lookUp(String ident) {
        return currentSymbolTable.lookup(ident);
    }

    // lookup type of given ident, starting from current scope
    public Type lookUpAll(String ident) {
        return currentSymbolTable.lookupAll(ident);
    }

    // add a new child scope to current scope's child list
    public void createNewScope() {
        currentSymbolTable = new SymbolTableScope(currentSymbolTable);

    }

    // at the end of one scope, close current scope (not delete) and move to parent scope
    public void backtraceScope() {
    }

    private class SymbolTableScope {
        private HashMap<String, Type> dictionary;
        private SymbolTableScope parentScope;
        private List<SymbolTableScope> childScopeList;

        public SymbolTableScope(SymbolTableScope parentScope) {
            this.dictionary = new HashMap<>();
            this.parentScope = parentScope;
            this.childScopeList = new ArrayList<>();

            if (parentScope != null) {
                parentScope.childScopeList.add(this);
            }
        }

        public void add(String name, Type type) {
            if (dictionary.containsKey(name)) {
                throw new IllegalArgumentException("redefinition of ident: " + name + " is not allowed");
            }
            this.dictionary.put(name, type);
        }

        public Type lookup(String name) {
            return dictionary.get(name);
        }

        public Type lookupAll(String name) {
            SymbolTableScope table = currentSymbolTable;
            Type obj = null;
            while(obj == null && table != null) {
                obj = table.dictionary.get(name);
                table = table.parentScope;
            }
            return obj;
        }
    }
}