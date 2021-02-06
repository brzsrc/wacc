package Node.Expr;

public class IdentNode extends ExprNode {

    private String ident;

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
