package node.expr;

import type.ArrayType;
import type.Type;

import java.util.List;

public class ArrayNode extends ExprNode {

    private int length, depth;
    private List<ExprNode> content;

    public ArrayNode(Type contentType, List<ExprNode> content, int length) {
        this.content = content;
        this.length = length;
        this.type = new ArrayType(contentType);

        /* calculate depth of nested array */
        Type t = contentType;
        depth = 1;
        while (t instanceof ArrayType) {
            t = ((ArrayType) t).getContentType();
            depth++;
        }
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
