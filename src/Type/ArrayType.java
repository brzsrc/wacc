package Type;

public class ArrayType implements Type {

    private Type type;

    public ArrayType(Type type) {
        this.type = type;
    }

    @Override
    public boolean equalToType(Type other) {
        if (!(other instanceof ArrayType)) {
            return false;
        }

        return type.equalToType(((ArrayType) other).getArrayType());
    }

    public Type getArrayType() {
        return type;
    }
    
}
