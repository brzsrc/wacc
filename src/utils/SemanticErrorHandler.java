package utils;

import java.util.List;
import java.util.Set;

import antlr.WACCParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import type.Type;

public class SemanticErrorHandler {

    public static final int SYNTAX_ERROR_CODE = 100;
    public static final int SEMANTIC_ERROR_CODE = 200;
    private static final int INTERNAL_ERROR_CODE = -1;

    public static void typeMismatch(ParserRuleContext ctx, Type expected, Type actual) {
        String msg = "Expected type " + expected.toString() + ", but the actual type is " + actual.toString();
        SemanticErrorHandler.errorHandler(ctx, SEMANTIC_ERROR_CODE, msg);
    }

    public static void typeMismatch(ParserRuleContext ctx, String ident, Type expected, Type actual) {
        String msg = "Expected type " + expected.toString() + " for variable " + ident + ", but the actual type is " + actual.toString();
        SemanticErrorHandler.errorHandler(ctx, SEMANTIC_ERROR_CODE, msg);
    }

    public static void typeMismatch(ParserRuleContext ctx, Set<Type> expected, Type actual) {
        String msg = "Expected types are " + expected.toString() + ", but the actual type is " + actual.toString();
        SemanticErrorHandler.errorHandler(ctx, SEMANTIC_ERROR_CODE, msg);
    }

    public static void invalidFuncArgCount(ParserRuleContext ctx, int expected, int actual) {
        String msg = "Invalid number of arguments: Expected " + expected + " argument(s), but actual number is " + actual;
        SemanticErrorHandler.errorHandler(ctx, SEMANTIC_ERROR_CODE, msg);
        System.exit(SEMANTIC_ERROR_CODE);
    }

    public static void symbolNotFound(ParserRuleContext ctx, String ident) {
        String msg = "Symbol " + ident + " is not found in the current scope of the program";
        SemanticErrorHandler.errorHandler(ctx, SEMANTIC_ERROR_CODE, msg);
        System.exit(SEMANTIC_ERROR_CODE);

    }

    public static void symbolRedeclared(ParserRuleContext ctx, String ident) {
        String msg = "Symbol " + ident + " has already been declared in the current scope of the program";
        SemanticErrorHandler.errorHandler(ctx, SEMANTIC_ERROR_CODE, msg);
    }

    public static void invalidFunctionReturnExit(ParserRuleContext ctx, String funcName) {
        String msg = "Function " + funcName + " has not returned or exited properly.";
        SemanticErrorHandler.errorHandler(ctx, SYNTAX_ERROR_CODE, msg);
        System.exit(SYNTAX_ERROR_CODE);
    }

    public static void functionJunkAfterReturn(WACCParser.SeqStatContext ctx) {
        String msg = "Function Junk exists.";
        SemanticErrorHandler.errorHandler(ctx, SYNTAX_ERROR_CODE, msg);
        System.exit(SYNTAX_ERROR_CODE);
    }

    public static void invalidRuleException(ParserRuleContext ctx, String visitorName) {
        String msg = "No matching rule for " + visitorName + ", bug in compiler";
        SemanticErrorHandler.errorHandler(ctx, INTERNAL_ERROR_CODE, msg);
        System.exit(INTERNAL_ERROR_CODE);

    }

    public static void arrayDepthError(ParserRuleContext ctx, Type type, int indexDepth) {
        String msg = "Array declared as " + type.toString() + ", but called with index depth " + indexDepth;
        SemanticErrorHandler.errorHandler(ctx, SEMANTIC_ERROR_CODE, msg);
        System.exit(SEMANTIC_ERROR_CODE);
    }

    public static void returnFromMainError(ParserRuleContext ctx) {
        String msg = "Call return in main function body";
        SemanticErrorHandler.errorHandler(ctx, SEMANTIC_ERROR_CODE, msg);
    }

    public static void invalidPairError(ParserRuleContext ctx) {
        String msg = "Calling fst/snd on uninitialised pair expr is not allowed";
        SemanticErrorHandler.errorHandler(ctx, SEMANTIC_ERROR_CODE, msg);
        System.exit(SEMANTIC_ERROR_CODE);
    }

    public static void integerRangeError(ParserRuleContext ctx, String intText) {
        String msg = "Integer " + intText + " format not compatible with 32bit int";
        SemanticErrorHandler.errorHandler(ctx, SYNTAX_ERROR_CODE, msg);
        System.exit(SYNTAX_ERROR_CODE);
    }

    private static void errorHandler(ParserRuleContext ctx, int code, String msg) {
        int lineNum;
        int linePos;

        /* funcJunk in SemanticChecker
         * symbolRedeclare in symbolTable checker
         * can only provide ctx as NULL */
        if (ctx == null) {
            System.err.println(msg);
            return;
        }

        if (ctx instanceof TerminalNode) {
            lineNum = ((TerminalNode) ctx).getSymbol().getLine();
            linePos = ((TerminalNode) ctx).getSymbol().getCharPositionInLine();
        } else {
            lineNum = ctx.getStart().getLine();
            linePos = ctx.getStart().getCharPositionInLine();
        }

        System.err.println("line " + lineNum + ":" + linePos + " : " + msg);
    }

}
