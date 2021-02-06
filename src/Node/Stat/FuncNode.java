package Node.Stat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Type.Type;
import utils.SymbolTable;

public class FuncNode {
    private Type returnType;
    private List<Type> parameters;
    private List<StatNode> functionBody;
    private SymbolTable functionSymbolTable;

    public FuncNode(Type returnType, List<StatNode> functionBody, List<Type> params) {
        this.returnType = returnType;
        this.functionBody = functionBody;
        this.parameters = params;
    }

    public List<StatNode> getBody() {
        return this.functionBody;
    }
}
