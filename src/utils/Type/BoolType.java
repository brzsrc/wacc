package utils.Type;

public class BoolType implements WACCType<Boolean> {
    private boolean value;

    public BoolType(boolean value) {
        this.value = value;
    }

    @Override
    public Boolean getValue() {
        return this.value;
    }

    @Override
    public void setValue(Boolean value) {
        this.value = value;
    }
}
