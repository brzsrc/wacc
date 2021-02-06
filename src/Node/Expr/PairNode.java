package Node.Expr;

/* Class definition for Pair */
class Pair<T, U> {
    private T fst;
    private U snd;

    public Pair() {
        this(null, null);
    }

    public Pair(T fst, U snd) {
        this.fst = fst;
        this.snd = snd;
    }

    public T getFst() {
        return fst;
    }

    public U getSnd() {
        return snd;
    }

    public void setFst(T fst) {
        this.fst = fst;
    }

    public void setSnd(U snd) {
        this.snd = snd;
    }
}

public class PairNode extends ExprNode {

    private ExprNode fst;
    private ExprNode snd;

    @Override
    public void setValue(String value) {
        throw new UnsupportedOperationException("ArrayNode does not support setting value by using raw string literals. Please pass ExprNode as the input!");
    }

    @Override
    public String getValue() {
        throw new UnsupportedOperationException("ArrayNode does not support getting value in the form of string. Please specify an index!");
    }

    @Override
    public boolean check() {
        return false;
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
