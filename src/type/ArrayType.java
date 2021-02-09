package type;

public class ArrayType implements Type {

    private Type contentType;

    public ArrayType(Type contentType) {
        this.contentType = contentType;
    }

    public ArrayType() {
        this(null);
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

    @Override
    public String toString() {
        return "Array<" + contentType + ">";
    }

}
