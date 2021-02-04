package utils.Node.Expr;

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

public class PairNode<T, U> extends ExprNode<Pair<ExprNode<T>, ExprNode<U>>> {

    // TODO: define PairNode

    @Override
    public boolean check() {
        return false;
    }
    
}
