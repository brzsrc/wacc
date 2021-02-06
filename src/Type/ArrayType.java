package Type;

public class ArrayType implements Type {

    Type type;

    public ArrayType(Type type) {
        this.type = type;
    }

    @Override
    public boolean equalToType(Type other) {
        if (!this.getClass().equals(other.getClass())) {
            return false;
        }

        return this.type.getClass().equals(((ArrayType) other).getArrayType().getClass());
    }

    public Type getArrayType() {
        return type;
    }

    @Override
    public String toString() {
        return "Array<" + type.toString() + ">";
    }
    
}
