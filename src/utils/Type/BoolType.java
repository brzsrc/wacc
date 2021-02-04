package utils.Type;

public class BoolType implements Type {

    @Override
    public boolean equalToType(Type other) {
        return this.getClass().equals(other.getClass());
    }

    @Override
    public String getTypeName() {
        return "Boolean";
    }
    
}
