package utils;

import org.antlr.v4.runtime.*;

public class ParserErrorHandler extends DefaultErrorStrategy {

    private static final int SYNTAX_ERROR_CODE = 100;

    @Override
    public void reportError(Parser recognizer, RecognitionException e) {
        super.reportError(recognizer, e);
        System.exit(SYNTAX_ERROR_CODE);
    }

    @Override
    public void recover(Parser recognizer, RecognitionException e) {
        super.recover(recognizer, e);
        recognizer.exitRule();
        System.exit(SYNTAX_ERROR_CODE);
    }

    @Override
    public Token recoverInline(Parser recognizer) {
        super.recoverInline(recognizer);
        recognizer.exitRule();
        System.exit(SYNTAX_ERROR_CODE);
        return null;
    }

    @Override
    public void sync(Parser recognizer) {
    }
}
