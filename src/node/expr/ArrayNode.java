package node.expr;

import type.ArrayType;
import type.Type;

import java.util.List;

public class ArrayNode extends ExprNode {

    private int length;
    private List<ExprNode> content;

    public ArrayNode(Type contentType, List<ExprNode> content, int length) {
        super("");
        this.content = content;
        this.length = length;
        this.type = new ArrayType(contentType);
    }

    @Override
    public void setValue(String value) {
        throw new UnsupportedOperationException("ArrayNode does not support setting value by using raw string literals. Please pass ExprNode as the input!");
    }

    @Override
    public String getValue() {
        throw new UnsupportedOperationException("ArrayNode does not support getting value in the form of string. Please specify an index!");
    }

    public int getLength() {
        return length;
    }

    /**
     * this function is used when a[] appear on the rhs,
     * lhs array elem assignment is represented by ArrayElemNode */
    public ExprNode getElem(int index) {
        return this.content.get(index);
    }

    public void setAllElem(List<ExprNode> content) {
        this.content = content;
        this.length = content.size();
    }

    public void setElem(int index, ExprNode value) {
        this.content.set(index, value);
    }

}
