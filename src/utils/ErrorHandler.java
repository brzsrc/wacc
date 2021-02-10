package utils;

import java.util.List;

import antlr.WACCParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import type.Type;

public class ErrorHandler {

    public static final int SYNTAX_ERROR_CODE = 100;
    public static final int SEMANTIC_ERROR_CODE = 200;
    public static final int INTEGER_MAX_VALUE = (int) Math.pow(2,31) - 1;
    public static final int INTEGER_MIN_VALUE = -(int) Math.pow(2,31);

    public void typeMismatch(ParserRuleContext ctx, Type expected, Type actual) {
        String msg = "Expected type " + expected.toString() + ", but the actual type is " + actual.toString();
        errorHandler(ctx, SEMANTIC_ERROR_CODE, msg);
    }

    public void typeMismatch(ParserRuleContext ctx, String ident, Type expected, Type actual) {
        String msg = "Expected type " + expected.toString() + " for variable " + ident + ", but the actual type is " + actual.toString();
        errorHandler(ctx, SEMANTIC_ERROR_CODE, msg);
    }

    public void typeMismatch(ParserRuleContext ctx, List<Type> expected, Type actual) {
        String msg = "Expected types are " + expected.toString() + ", but the actual type is " + actual.toString();
        errorHandler(ctx, SEMANTIC_ERROR_CODE, msg);
    }

    public void invalidFuncArgCount(ParserRuleContext ctx, int expected, int actual) {
        String msg = "Invalid number of arguments: Expected " + expected + " argument(s), but actual number is " + actual;
        errorHandler(ctx, SEMANTIC_ERROR_CODE, msg);
    }

    public void symbolNotFound(ParserRuleContext ctx, String ident) {
        String msg = "Symbol " + ident + " is not found in the current scope of the program";
        errorHandler(ctx, SEMANTIC_ERROR_CODE, msg);
    }

    public void symbolRedeclared(ParserRuleContext ctx, String ident) {
        String msg = "Symbol " + ident + " has already been declared in the current scope of the program";
        errorHandler(ctx, SEMANTIC_ERROR_CODE, msg);
    }

    public void invalidFunctionReturnExit(ParserRuleContext ctx, String funcName) {
        String msg = "Function " + funcName + " has not returned or exited properly.";
        errorHandler(ctx, SYNTAX_ERROR_CODE, msg);
    }

    public void functionJunkAfterReturn(WACCParser.SeqStatContext ctx) {
        String msg = "Function Junk exists.";
        errorHandler(ctx, SYNTAX_ERROR_CODE, msg);
    }

    public void invalidRuleException(ParserRuleContext ctx, String visitorName) {
        String msg = "No matching rule for " + visitorName + ", bug in compiler";
        errorHandler(ctx, 0, msg);
    }

    public void arrayDepthError(ParserRuleContext ctx, Type type, int indexDepth) {
        String msg = "Array declared as " + type.toString() + ", but called with index depth " + indexDepth;
        errorHandler(ctx, SEMANTIC_ERROR_CODE, msg);
    }

    public void returnFromMainError(ParserRuleContext ctx) {
        String msg = "Call return in main function body";
        errorHandler(ctx, SEMANTIC_ERROR_CODE, msg);
    }

    public void invalidPairError(ParserRuleContext ctx) {
        String msg = "Calling fst/snd on uninitialised pair expr is not allowed";
        errorHandler(ctx, SEMANTIC_ERROR_CODE, msg);
    }

    public void integerRangeError(ParserRuleContext ctx, String intText) {
        String msg = "Integer " + intText + " format not compatible with 32bit int";
        errorHandler(ctx, SYNTAX_ERROR_CODE, msg);
    }

    // todo: only internal error should terminate compiling, other error should continue and parse the rest stat
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

        System.err.println("line " + lineNum + ":" + linePos + " : " + msg);
        System.exit(code);
    }



}
