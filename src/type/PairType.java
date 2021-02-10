package type;

public class PairType implements Type {

    private Type fstType;
    private Type sndType;
    
    public PairType(Type fstType, Type sndType) {
        this.fstType = fstType;
        this.sndType = sndType;
    }

    public PairType() {
        this(null, null);
    }

    public Type getFstType() {
        return fstType;
    }

    public Type getSndType() {
        return sndType;
    }

    public void setFstType(Type fstType) {
        this.fstType = fstType;
    }

    public void setSndType(Type sndType) {
        this.sndType = sndType;
    }

    @Override
    public PairType asPairType() {
        return this;
    }

    @Override
    public boolean equalToType(Type other) {
        if (other == null) {
            return true;
        }
        if (!(other instanceof PairType)) {
            return false;
        }

        PairType otherPair = (PairType) other;

        return subTypeCompact(fstType, otherPair.fstType)
                && subTypeCompact(sndType, otherPair.sndType);
    }

    private boolean subTypeCompact(Type type1, Type type2) {
        if (type1 == null || type2 == null) {
            // type1 is null indicate current type is Pair, a branch of Pair(Pair, _)
            // this happens when calling fst x = ...
            // allow any type, since this cause a cast.
            return true;
        } else if (type1 instanceof PairType) {
            // no matter what type lhs was assigned, since type can be casted,
            // the sub type only need to satisfy it is also a pair
            return type2 instanceof PairType;
        }

        // base/array type check
        return type1.equalToType(type2);
    }

    @Override
    public String toString() {
        return "Pair<" + fstType + ", " + sndType +">";
    }
    
}
