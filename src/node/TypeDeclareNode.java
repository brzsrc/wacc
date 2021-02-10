package node;

import type.Type;

public class TypeDeclareNode implements Node {
    private Type declaredType;

    public TypeDeclareNode(Type declaredType) {
        this.declaredType = declaredType;
    }

    public Type getType() {
        return declaredType;
    }

    @Override
    public TypeDeclareNode asTypeDeclareNode() {
        return this;
    }
}
