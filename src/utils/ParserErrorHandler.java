package utils;

import org.antlr.v4.runtime.*;

import static utils.ErrorHandler.SYNTAX_ERROR_CODE;

public class ParserErrorHandler extends DefaultErrorStrategy {

    @Override
    public void recover(Parser recognizer, RecognitionException e) {
        System.exit(SYNTAX_ERROR_CODE);
    }

    @Override
    public Token recoverInline(Parser recognizer) {
        System.exit(SYNTAX_ERROR_CODE);
        return null;
    }
}
