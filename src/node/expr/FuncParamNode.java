package node.expr;

import type.Type;
import utils.SymbolTable;

public class FuncParamNode extends ExprNode {

    private String name;

    public FuncParamNode(Type type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public Type getType(SymbolTable symbolTable) {
        return symbolTable.lookupAll(name).type;
    }
    
}
