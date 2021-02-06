package Node.Expr;

public class IdentNode extends ExprNode {

    private String ident;

    @Override
    public boolean check() {
        // TODO Auto-generated method stub
        return false;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }
    
}
