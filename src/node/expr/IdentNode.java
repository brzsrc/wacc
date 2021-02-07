package node.expr;

import type.Type;
import utils.SymbolTable;

public class IdentNode extends ExprNode {

    public IdentNode(String value) {
        super(value);
    }

    @Override
    public Type getType(SymbolTable symbolTable) {
        return symbolTable.lookupAll(value).type;
    }

    public String getIdent() {
        return value;
    }

    public void setIdent(String ident) {
        this.value = ident;
    }
    
}
