package lang.colours;

import lang.ParseException;
import lex.Lexer;

import java.io.IOException;

/**
 * Colours     -> BEGIN ColourList END
 *
 * ColourList  -> Colour ColourList
 * ColourList  ->
 *
 * Colour      -> RED
 * Colour      -> GREEN
 * Colour      -> BLUE
 *
 */

public class ColoursParser {

    private Lexer lex;

    public ColoursParser() {
    }

    public void parse(String filePath) throws IOException {
        lex = new Lexer(ColoursTokens.DEFS);
        lex.readFile(filePath);
        lex.next();
        Colours();
        eof(); // the parse must fail if there is any "junk" at the end of the input file
        System.out.println("Parse succeeded.");
    }

    public void Colours() {

        eat("BEGIN");
            ColourList();
        eat("END");
    }

    public void ColourList() {
        switch (lex.tok().type) {
            // ColourList -> Colour ColourList
            case "RED", "GREEN", "BLUE":
                // Fix this case; it should list the selection set for the
                // first ColourList rule.
                Colour();
                ColourList();
                break;
            // ColourList  ->
            default:
                // For a rule with empty right-hand-side, just select by default.
                // We don't need to check the selection set for an LL(1)
                // grammar because the parser will do appropriate checks when
                // parsing whatever follows this empty expansion.
        }
    }

    public void Colour() {

        switch (lex.tok().type){

            case "RED":
                System.out.println("Red");
                lex.next();
                break;

            case "GREEN":
                System.out.println("Green");
                lex.next();
                break;

            case "BLUE":
                System.out.println("Blue");
                lex.next();
                break;

            default:
                throw new ParseException("TOKENS: {RED, GREEN, BLUE} are not locatable");
        }
    }

    /**
     * Verify that the token stream is at EOF.
     * @throws ParseException if token stream is not at EOF
     */
    private void eof() {
        if (lex.tok().type != "EOF") {
            throw new ParseException(lex.tok(), "EOF");
        }
    }

    /**
     * Check the head token and, if it matches, advance to the next token.
     * @param type the token type that we expect
     * @return the text of the head token that was matched
     * @throws ParseException if the head token does not match.
     */
    public String eat(String type) {
        if (type.equals(lex.tok().type)) {
            String image = lex.tok().image;
            lex.next();
            return image;
        } else {
            throw new ParseException(lex.tok(), type);
        }
    }

    public static void main(String[] args) throws IOException {
        ColoursParser parser = new ColoursParser();
        parser.parse(args[0]);
    }
}
