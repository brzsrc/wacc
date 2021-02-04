package utils.Type;

public class PairType<T extends Type, U extends Type> implements Type {

    T fstType;
    U sndType;
    
    public PairType(T fstType, U sndType) {
        this.fstType = fstType;
        this.sndType = sndType;
    }

    @Override
    public String getTypeName() {
        return "Pair<" + fstType.getTypeName() + ", " + sndType.getTypeName() + ">";
    }

    @Override
    public boolean equalToType(Type other) {
        // TODO: solve the pair type coaleace problem
        return false;
    }
    
}
