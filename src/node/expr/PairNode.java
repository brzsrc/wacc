package node.expr;

import type.PairType;

public class PairNode extends ExprNode {

    private ExprNode fst;
    private ExprNode snd;

    public PairNode(ExprNode fst, ExprNode snd) {
        super("");
        this.fst = fst;
        this.snd = snd;
        this.type = new PairType(fst.type, snd.type);
    }

    @Override
    public void setValue(String value) {
        throw new UnsupportedOperationException("ArrayNode does not support setting value by using raw string literals. Please pass ExprNode as the input!");
    }

    @Override
    public String getValue() {
        throw new UnsupportedOperationException("ArrayNode does not support getting value in the form of string. Please specify an index!");
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
