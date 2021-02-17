package frontend.node;

import frontend.type.Type;

public class TypeDeclareNode implements Node {

  /**
   * Represent a frontend.type declaration. It is neither an ExprNode nor a StatNode This class is used when
   * visiting a base_type, array_type, or pair_type, and will help to determine the frontend.type of the
   * declared variable
   *
   * Example: int, char, bool
   */

  private final Type declaredType;

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
