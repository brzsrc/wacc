package type;

public class BasicType implements Type {
    private BasicTypeEnum basicTypeEnum;

    public BasicType(BasicTypeEnum basicTypeEnum) {
        this.basicTypeEnum = basicTypeEnum;
    }

    @Override
    public boolean equalToType(Type other) {
        if (!(other instanceof BasicType)) {
            return false;
        }

        return basicTypeEnum.equals(((BasicType) other).getTypeEnum());
    }

    public BasicTypeEnum getTypeEnum() {
        return basicTypeEnum;
    }

    @Override
    public String toString() {
        return basicTypeEnum.toString();
    }
}
