package Node.Expr;

public class ArrayElemNode<T> extends ExprNode<T> {

    private String ident;
    private ExprNode<T> expr;

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

    public ExprNode<T> getExpr() {
        return expr;
    }

    public void setExpr(ExprNode<T> expr) {
        this.expr = expr;
    }
    
}
