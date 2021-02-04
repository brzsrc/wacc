package utils.Node.Expr;

public class IdentNode<T> extends ExprNode<T> {

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
