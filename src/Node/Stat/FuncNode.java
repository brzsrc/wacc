package Node.Stat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Node.Expr.FuncParamNode;
import Type.Type;

public class FuncNode {
    public Type returnType;
    public List<FuncParamNode> parameters;
    public List<StatNode> functionBody;

    public FuncNode(Type returnType, List<StatNode> functionBody, FuncParamNode params) {
        this.returnType = returnType;
        this.functionBody = functionBody;
        this.parameters = new ArrayList<>(Arrays.asList(params));
    }

    public List<StatNode> getBody() {
        return this.functionBody;
    }
}
