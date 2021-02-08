package node;

import node.expr.FuncParamNode;
import node.stat.StatNode;
import java.util.List;

import type.Type;
import utils.SymbolTable;

// this node is not one statement but one function definition, no need to implement statNode
public class FuncNode implements Node {
    private Type returnType;
    private List<FuncParamNode> parameters;
    private StatNode functionBody;

    public FuncNode(Type returnType, StatNode functionBody, List<FuncParamNode> params) {
        this.returnType = returnType;
        this.functionBody = functionBody;
        this.parameters = params;
    }

    public StatNode getFunctionBody() {
        return functionBody;
    }

    public Type getReturnType() {
        return returnType;
    }

    public List<FuncParamNode> getParamList() {
        return parameters;
    }
}
