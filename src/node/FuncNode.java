package node;

import node.expr.IdentNode;
import node.stat.StatNode;
import java.util.List;

import type.Type;

// this node is not one statement but one function definition, no need to implement statNode
public class FuncNode implements Node {
    private Type returnType;
    private List<IdentNode> parameters;
    private StatNode functionBody;

    public FuncNode(Type returnType, List<IdentNode> params) {
        this(returnType, null, params);
    }

    public FuncNode(Type returnType, StatNode functionBody, List<IdentNode> params) {
        this.returnType = returnType;
        this.functionBody = functionBody;
        this.parameters = params;
    }

    public StatNode getFunctionBody() {
        return functionBody;
    }

    public void setFunctionBody(StatNode functionBody) {
        this.functionBody = functionBody;
    }

    public Type getReturnType() {
        return returnType;
    }

    public List<IdentNode> getParamList() {
        return parameters;
    }
}
