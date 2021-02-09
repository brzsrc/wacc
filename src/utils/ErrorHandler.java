package utils;

import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import type.Type;

public class ErrorHandler {

    public static final int SYNTAX_ERROR_CODE = 100;
    public static final int SEMANTIC_ERROR_CODE = 200;
    public static final int INTEGER_MAX_VALUE = (int) Math.pow(2,31) - 1;
    public static final int INTEGER_MIN_VALUE = -(int) Math.pow(2,31);

    public void typeMismatch(ParserRuleContext ctx, Type expected, Type actual) {
        String msg = "Expected type " + expected.toString() + " for variable x, but the actual type is " + actual.toString();
        errorHandler(ctx, SEMANTIC_ERROR_CODE, msg);
    }

    public void typeMismatch(ParserRuleContext ctx, List<Type> expected, Type actual) {
        String msg = "Expected types are " + expected.toString() + " for variable x, but the actual type is " + actual.toString();
        errorHandler(ctx, SEMANTIC_ERROR_CODE, msg);
    }

    public void invalidFuncArgCount(ParserRuleContext ctx, int expected, int actual) {
        String msg = "Invalid number of arguments: blahblahblah";
        errorHandler(ctx, SEMANTIC_ERROR_CODE, msg);
    }

    public void symbolNotFound(ParserRuleContext ctx, String ident) {
        String msg = "Symbol " + ident + " is not found in the current scope of the program";
        errorHandler(ctx, SEMANTIC_ERROR_CODE, msg);
    }

    public void symbolRedeclared(ParserRuleContext ctx, String ident) {
        String msg = "Symbol x has already been declared in the current scope of the program";
        errorHandler(ctx, SEMANTIC_ERROR_CODE, msg);
    }

    public void invalidFunctionReturnExit(ParserRuleContext ctx, String funcName) {
        String msg = "Function " + funcName + " has not returned or exited properly.";
        errorHandler(ctx, SYNTAX_ERROR_CODE, msg);
    }

    private void errorHandler(ParserRuleContext ctx, int code, String msg) {
        int lineNum = 0;
        int linePos = 0;

        if (ctx instanceof TerminalNode) {
            lineNum = ((TerminalNode) ctx).getSymbol().getLine();
            linePos = ((TerminalNode) ctx).getSymbol().getCharPositionInLine();
        } else if (ctx instanceof ParserRuleContext) {
            lineNum = ((ParserRuleContext) ctx).getStart().getLine();
            linePos = ((ParserRuleContext) ctx).getStart().getCharPositionInLine();
        } else {
            /* internal error */
        }

        System.out.println("Line " + lineNum + ":" + linePos + msg);
        System.exit(code);
    }
}
