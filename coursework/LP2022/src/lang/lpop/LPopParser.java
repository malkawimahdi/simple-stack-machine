package lang.lpop;

import lang.ParseException;
import lang.lpse.LPseParser;
import lex.Lexer;

import java.io.IOException;

public class LPopParser {

    private Lexer lex;

    public LPopParser() {
    }

    public void parse(String filePath) throws IOException {
        lex = new Lexer(LPopTokens.DEFS);
        lex.readFile(filePath);
        lex.next();
        // to be completed
        System.out.println("Parse succeeded.");
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
        LPopParser parser = new LPopParser();
        parser.parse(args[0]);
    }
}
