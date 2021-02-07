package type;

public class ArrayType implements Type {

    private Type contentType;

    public ArrayType(Type contentType) {
        this.contentType = contentType;
    }

    @Override
    public boolean equalToType(Type other) {
        if (!(other instanceof ArrayType)) {
            return false;
        }

        return contentType.equalToType(((ArrayType) other).getContentType());
    }

    public Type getContentType() {
        return contentType;
    }

}
