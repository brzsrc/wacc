package node.expr;

import java.util.List;

public class ArrayNode extends ExprNode {

    private int length;
    private List<ExprNode> content;

    @Override
    public boolean check() {
        return false;
    }

    @Override
    public void setValue(String value) {
        throw new UnsupportedOperationException("ArrayNode does not support setting value by using raw string literals. Please pass ExprNode as the input!");
    }

    @Override
    public String getValue() {
        throw new UnsupportedOperationException("ArrayNode does not support getting value in the form of string. Please specify an index!");
    }

    public void setValue(List<ExprNode> content) {
        this.content = content;
        this.length = content.size();
    }

    public ExprNode getValue(int index) {
        return this.content.get(index);
    }

    public void setValue(int index, ExprNode value) {
        this.content.set(index, value);
    }

    public int getLength() {
        return length;
    }

    public ArrayNode(List<ExprNode> content, int length) {
        this.content = content;
        this.length = length;
    }
    
}
