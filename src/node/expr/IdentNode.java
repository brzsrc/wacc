package node.expr;

import type.Type;
import utils.SymbolTable;

public class IdentNode extends ExprNode {

    private String name;

    public IdentNode(Type type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public Type getType(SymbolTable symbolTable) {
        return symbolTable.lookupAll(name).type;
    }
    
}
