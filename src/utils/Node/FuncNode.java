package utils.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import utils.Type.WACCType;

public class FuncNode<> {
    public WACCType<T> returnType;
    public List<WACCType> parameters;
    public List<Instruction> functionBody;

    public FuncType(WACCType returnType, List<Instruction> functionBody, WACCType... params) {
        this.returnType = returnType;
        this.functionBlody = functionBody;
        this.parameters = new ArrayList<>(Arrays.asList(params));
    }

    /** return function instance, implement ord unary operator */
    public static FuncType ord(CharType expr) {
        return new FuncType(IntegerType.defaultInstance(), null, expr);
    }

    /** return function instance, implement chr unary operator */
    public static FuncType chr(IntegerType expr) {
        return new FuncType(IntegerType.defaultInstance(), null, expr);
    }
}
