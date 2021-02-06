package utils;

import Node.Expr.IdentNode;
import Node.Stat.FuncNode;
import Type.Type;
import antlr.WACCParser;

public class ErrorHandler {

    public static final int SYNTAX_ERROR_CODE = 100;
    public static final int SEMANTIC_ERROR_CODE = 200;
    public static final int INTEGER_MAX_VALUE = (int) Math.pow(2,31) - 1;
    public static final int INTEGER_MIN_VALUE = -(int) Math.pow(2,31);

    public void typeMismatch(WACCParser parser, Type expected, Type actual) {
        String msg = "Expected type " + expected.toString() + " for variable x, but the actual type is " + actual.toString;
        errorHandler(parser, SEMANTIC_ERROR_CODE, msg);
    }

    public void invalidFuncArgCount(WACCParser parser, int expected, int actual) {
        String msg = "Invalid number of arguments: blahblahblah";
        errorHandler(parser, SEMANTIC_ERROR_CODE, msg);
    }

    public void symbolNotFound(WACCParser parser, IdentNode ident) {
        String msg = "Symbol x is not found in the current scope of the program";
        errorHandler(parser, SEMANTIC_ERROR_CODE, msg);
    }

    public void symbolRedeclared(WACCParser parser, IdentNode ident) {
        String msg = "Symbol x has already been declared in the current scope of the program";
        errorHandler(parser, SEMANTIC_ERROR_CODE, msg);
    }

    private void errorHandler(WACCParser parser, int code, String msg) {
        System.out.println("Line6:7 " + msg);
        System.exit(code);
    }
}
