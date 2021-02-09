package node.expr;

import type.PairType;

public class PairNode extends ExprNode {

    /* uninitialised pair will be represented as pair(null, null)
     * newpair(null, null) will be represented as pair(pair(null, null), pair(null, null)) */
    private ExprNode fst;
    private ExprNode snd;

    public PairNode(ExprNode fst, ExprNode snd) {
        this.fst = fst;
        this.snd = snd;
        this.type = new PairType(fst.type, snd.type);
    }

    public PairNode() {
        this.fst = null;
        this.snd = null;
        this.type = new PairType();
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
