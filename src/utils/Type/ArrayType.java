package utils.Type;

import java.util.List;
import utils.Type.Type;

public class ArrayType<T extends Type> implements Type {

    private String id; // array a[] has id 'a'
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

    public String getId() {
        return id;
    }

    @Override
    public String getTypeName() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
