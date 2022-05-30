package lang.teeny;

import lang.ParseException;
import lex.Lexer;

import java.io.IOException;


/**
 * -- Grammar for a teeny-weeny language.
 * --------------------------------------
 *
 * Prog -> BEGIN Stm* END
 *
 * Stm  -> IF LBR Exp RBR Stm ELSE Stm
 * Stm  -> PRINTINT Exp SEMIC
 * Stm  -> PRINTCHAR Exp SEMIC
 * Stm  -> LCBR Stm* RCBR
 *
 * Exp  -> INT
 *
 */

public class TeenyWeenyParser {

    private Lexer lex;

    public TeenyWeenyParser() {
    }

    public void parse(String filePath) throws IOException {
        lex = new Lexer(TeenyWeenyTokens.DEFS);
        lex.readFile(filePath);
        lex.next();
        Prog();
        System.out.println("Parse succeeded.");
    }

    public void Prog() {
        // Prog -> BEGIN Stm* END
        eat("BEGIN");
        while (lex.tok().type != "END") {
            Stm();
        }
        eat("END");
        eof(); // the parse will fail if there is any "junk" after the END token
    }

    public void Stm() {
        switch (lex.tok().type) {
            // Stm -> PRINTINT Exp SEMIC
            case "PRINTINT":
                lex.next();
                Exp();
                eat("SEMIC");
                break;
            // Stm -> PRINTCHAR Exp SEMIC
            case "PRINTCHAR":
                lex.next();
                Exp();
                eat("SEMIC");
                break;
            // Stm -> IF LBR Exp RBR Stm ELSE Stm
            case "IF":
                lex.next();
                eat("LBR");
                Exp();
                eat("RBR");
                Stm();
                eat("ELSE");
                Stm();
                break;
                // Stm -> LCBR Stm* RCBR
            case "LCBR":
                lex.next();
                while (lex.tok().type != "RCBR") {
                    Stm();
                }
                lex.next();
                break;
            default:
                throw new ParseException(lex.tok(), "PRINTINT", "PRINTCHAR", "IF", "LCBR");
        }
    }

    public void Exp() {
        // Exp -> INT
        eat("INT");
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
        TeenyWeenyParser parser = new TeenyWeenyParser();
        parser.parse(args[0]);
    }
}
