package utils.Node.Expr;

import java.util.List;

public class ArrayNode<T> extends ExprNode<List<T>> {

    private int length;

    @Override
    public boolean check() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setValue(List<T> content) {
        this.value = content;
        this.length = content.size();
    }

    public T getValue(int index) {
        return this.value.get(index);
    }

    public void setValue(int index, T value) {
        this.value.set(index, value);
    }

    public int getLength() {
        return length;
    }

    public ArrayNode(List<T> content, int length) {
        this.value = content;
        this.length = length;
    }
    
}
