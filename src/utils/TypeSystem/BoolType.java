package utils.TypeSystem;

public class BoolType implements TypeSystem {
    public boolean bVal;
    public BoolType(boolean bVal) {
        this.bVal = bVal;
    }

    public static BoolType defaultInstance() {
        return new BoolType(false);
    }
    
}
