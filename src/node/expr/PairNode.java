package node.expr;

import type.PairType;

public class PairNode extends ExprNode {

    private ExprNode fst;
    private ExprNode snd;

    public PairNode(ExprNode fst, ExprNode snd) {
        this.fst = fst;
        this.snd = snd;
        this.type = new PairType(fst.type, snd.type);
    }

    public ExprNode getFst() {
        return fst;
    }

    public ExprNode getSnd() {
        return snd;
    }

    public void setFst(ExprNode fst) {
        this.fst = fst;
    }

    public void setSnd(ExprNode snd) {
        this.snd = snd;
    }
    
}
