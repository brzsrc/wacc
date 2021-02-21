package utils.frontend;

import org.antlr.v4.runtime.*;

import static utils.frontend.Utils.*;

public class ParserErrorHandler extends DefaultErrorStrategy {

  /**
   * ParserErrorHandler will handle syntax errors reported by ANTLR parser by reporting the errors
   * and then exit with SYNTAX_ERROR_CODE
   */

  /* override reportError to properly exit the program after printing syntax error */
  @Override
  public void reportError(Parser recognizer, RecognitionException e) {
    super.reportError(recognizer, e);
    System.exit(SYNTAX_ERROR_CODE);
  }

  /* override recoverInLine to properly exit the program after the ANTLR missingSymbol error */
  @Override
  public Token recoverInline(Parser recognizer) {
    super.recoverInline(recognizer);
    recognizer.exitRule();
    System.exit(SYNTAX_ERROR_CODE);
    return null;
  }

  /* override reportUnwantedToken to properly exit the program after the ANTLR extraneousInput error */
  @Override
  protected void reportUnwantedToken(Parser recognizer) {
    super.reportUnwantedToken(recognizer);
    System.exit(SYNTAX_ERROR_CODE);
  }
}
