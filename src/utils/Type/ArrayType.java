package utils.Type;

import java.util.List;
import utils.Type.Type;

public class ArrayType<T extends Type> implements Type {

    T arrayType;

    public ArrayType(T arrayType) {
        this.arrayType = arrayType;
    }

    @Override
    public boolean equalToType(Type other) {
        if (!this.getClass().equals(other.getClass())) {
            return false;
        }
        // TODO: need better code here
        ArrayType otherArray = (ArrayType) other;
        return this.arrayType.getClass().equals(otherArray.getArrayType().getClass());
    }

    public T getArrayType() {
        return arrayType;
    }

    @Override
    public String getTypeName() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
