package node.expr;

import node.Node;
import type.Type;
import utils.SymbolTable;

public abstract class ExprNode implements Node {
    protected Type type;

    public Type getType(SymbolTable symbolTable) {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }
    
    public boolean check() {
        return false;
    }
}
