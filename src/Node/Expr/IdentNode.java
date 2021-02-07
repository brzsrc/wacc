package Node.Expr;

import Node.Stat.ScopeNode;
import Type.Type;
import utils.SymbolTable;

public class IdentNode extends ExprNode {

    private String ident;

    @Override
    public Type getType(SymbolTable symbolTable) {
        return symbolTable.lookupAll(ident);
    }

    @Override
    public boolean check() {
        return false;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }
    
}
