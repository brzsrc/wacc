package type;

public class ArrayType implements Type {

    private Type contentType;
    private int depth;

    public ArrayType(Type contentType) {
        this.contentType = contentType;

        Type subType = contentType;
        int depth = 1;
        while (subType instanceof ArrayType) {
            subType = subType.asArrayType().getContentType();
            depth++;
        }
        this.depth = depth;
    }

    public ArrayType() {
        this(null);
	}

	@Override
    public boolean equalToType(Type other) {
        if (other == null) {
            return true;
        }
        if (!(other instanceof ArrayType)) {
            return false;
        }

        return contentType.equalToType(((ArrayType) other).getContentType());
    }

    public int getDepth() {
        return depth;
    }

    public Type getContentType() {
        return contentType;
    }

    @Override
    public String toString() {
        return "Array<" + contentType + ">";
    }

    @Override
    public ArrayType asArrayType() {
        return this;
    }
}
