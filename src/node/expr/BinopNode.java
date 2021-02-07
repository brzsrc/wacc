package node.expr;

enum Binops {
    PLUS, MINUS, MUL, DIV, MOD, GREATER, GREATER_EQUAL, LESS, LESS_EQUAL, EQUAL, UNEQUAL, AND, OR
}

public class BinopNode extends ExprNode {

    private ExprNode expr1;
    private ExprNode expr2;
    private Binops operator;

    public BinopNode(ExprNode expr1, ExprNode expr2, Binops operator) {
        this.expr1 = expr1;
        this.expr2 = expr2;
        this.operator = operator;
    }

    @Override
    public boolean check() {
        return false;
    }

    @Override
    public void setValue(String value) {
        throw new UnsupportedOperationException("Binop does not support setting its value. Please pass ExprNode as the input!");
    }

    @Override
    public String getValue() {
        throw new UnsupportedOperationException("Binop does not support return of its value yet. Please specify an index!");
    }

    public ExprNode getExpr1() {
        return expr1;
    }

    public void setExpr1(ExprNode expr1) {
        this.expr1 = expr1;
    }

    public ExprNode getExpr2() {
        return expr2;
    }

    public void setExpr2(ExprNode expr2) {
        this.expr2 = expr2;
    }

    public Binops getOperator() {
        return operator;
    }

    public void setOperator(Binops operator) {
        this.operator = operator;
    }

}
