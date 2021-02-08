package node;

import type.Type;

public class TypeDeclareNode {
    private Type declaredType;

    public TypeDeclareNode(Type declaredType) {
        this.declaredType = declaredType;
    }

    public Type getType() {
        return declaredType;
    }
}
