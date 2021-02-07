package Node;

import Node.Stat.StatNode;
import java.util.List;

import Type.Type;

// this node is not one statement but one function definition, no need to implement statNode
public class FuncNode implements Node {
    private Type returnType;
    private List<Type> parameters;
    private StatNode functionBody;

    public FuncNode(Type returnType, StatNode functionBody, List<Type> params) {
        this.returnType = returnType;
        this.functionBody = functionBody;
        this.parameters = params;
    }

    public StatNode getFunctionBody() {
        return functionBody;
    }
}
