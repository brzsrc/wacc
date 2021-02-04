package utils.Type;

public class PairType<T extends WACCType<T>, U extends WACCType<U>> implements WACCType<PairType<T, U>> {
    private T fst;
    private U snd;

    public PairType(T fst, U snd) {
        this.fst = fst;
        this.snd = snd;
    }

    @Override
    public PairType<T, U> getValue() {
        return this;
    }

    @Override
    public void setValue(PairType<T, U> value) {
        this.fst = value.getFst();
        this.snd = value.getSnd();
    }

    public T getFst() {
        return this.fst;
    }

    public U getSnd() {
        return this.snd;
    }

    public void setFst(T fst) {
        this.fst = fst;
    }

    public void setSnd(U snd) {
        this.snd = snd;
    }
}
