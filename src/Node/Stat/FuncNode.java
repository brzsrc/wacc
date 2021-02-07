package Node.Stat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Type.Type;
import utils.SymbolTable;

// this node is not one statement but one function definition, no need to implement statNode
public class FuncNode {
    private Type returnType;
    private List<Type> parameters;
    private ScopeNode functionBody;

    public FuncNode(Type returnType, ScopeNode functionBody, List<Type> params) {
        this.returnType = returnType;
        this.functionBody = functionBody;
        this.parameters = params;
    }

    public List<StatNode> getBody() {
        return this.functionBody.getAllStat();
    }
}
