package utils;

import org.antlr.v4.runtime.*;

public class ParserErrorHandler extends DefaultErrorStrategy {

    public static final int SYNTAX_ERROR_CODE = 100;
    
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
