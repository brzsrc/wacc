package utils.Type;

import java.util.List;
import utils.Type.WACCType;

public class ArrayType<T extends WACCType<T>> implements WACCType<List<T>> {
    private int length;
    private List<T> content;

    public ArrayType(List<T> content) {
        this.content = content;
        this.length = content.size();
    }

    @Override
    public List<T> getValue() {
        return this.content;
    }

    public T getValue(int index) {
        return this.content.get(index);
    }

    @Override
    public void setValue(List<T> value) {
        this.content = value;
    }

    public void setValue(int index, T value) {
        if (index >= length) {
            /* Error handling */
            return;
        }
        this.content.set(index, value);
    }
}
