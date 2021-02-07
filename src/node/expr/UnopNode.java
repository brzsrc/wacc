package node.expr;

enum Unop {
    NOT, MINUX, LEN, ORD, CHR
}

public class UnopNode extends ExprNode {

    ExprNode expr;
    Unop operator;

    public UnopNode(ExprNode expr, Unop operator) {
        this.expr = expr;
        this.operator = operator;
    }

    @Override
    public boolean check() {
        return false;
    }

}
