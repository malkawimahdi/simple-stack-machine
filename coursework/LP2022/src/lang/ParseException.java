package lang;

import lex.Token;

public class ParseException extends RuntimeException {

    /**
     * Initialise a ParseException with a custom error message.
     * @param errMsg the error message
     */
    public ParseException(String errMsg) {
        super(errMsg);
    }

    /**
     * Initialise a ParseException.
     * @param actual the token at which the parse failed
     * @param expected all the token types that the parser would have accepted
     */
    public ParseException(Token actual, String... expected) {
        super(message(actual, expected));
    }

    private static String message(Token actual, String... expected) {
        if (expected.length == 0) {
            return "Unexpected token " + actual + " on line " + actual.lineNumber;
        } else if (expected.length == 1) {
            return "Expected " + expected[0] + " but found " + actual + " on line " + actual.lineNumber;
        } else {
            String exp = expected[0];
            for (int i = 1; i < expected.length; ++i) exp = exp + ", " + expected[i];
            return "Expected one of " + exp + " but found " + actual + " on line " + actual.lineNumber;
        }
    }
}