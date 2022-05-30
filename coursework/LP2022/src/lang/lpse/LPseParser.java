package lang.lpse;

import lang.ParseException;
import lex.Lexer;

public class LPseParser {

    private Lexer lex;

    public LPseParser() {
    }

    public static void main(String[] args) throws Exception {
        LPseParser parser = new LPseParser();
        parser.parse(args[0]);
    }

    public void parse(String filePath) throws Exception {
        lex = new Lexer(LPseTokens.DEFS);
        lex.readFile(filePath);
        lex.next();
        Prog();
        System.out.println("Parse succeeded.");
    }

    public void Prog() {
        eat("BEGIN");
        while (lex.tok().type != "END") {
            Stm();
        }
        eat("END");
    }

    private void Stm() {
        switch (lex.tok().type) {
            case "PRINTINT":
                lex.next();
                Exp();
                eat("SEMIC");
                break;

            case "PRINTCHAR":
                lex.next();
                Exp();
                eat("SEMIC");
                break;

            case "WHILE":
                lex.next();
                eat("LBR");
                Exp();
                eat("RBR");
                eat("DO");
                while (lex.tok().type != "DONE") {
                    Stm();
                }
                eat("DONE");
                break;

            case "IF":
                lex.next();
                eat("LBR");
                Exp();
                eat("RBR");
                eat("THEN");
                while (lex.tok().type != "ELSE") {
                    Stm();
                }
                eat("ELSE");
                while (lex.tok().type != "ENDIF") {
                    Stm();
                }
                eat("ENDIF");
                break;

            case "ID":
                lex.next();
                eat("EQUALS");
                Exp();
                eat("SEMIC");
                break;
        }
    }

    private void Exp() {
        BasicExp();
        ExpRest();
    }

    private void BasicExp() {
        switch (lex.tok().type) {
            case "INT":
                eat("INT");
                break;

            case "ID":
                eat("ID");
                break;

            case "LBR":
                eat("LBR");
                Exp();
                eat("RBR");
                break;
        }
    }

    private void ExpRest() {
        switch (lex.tok().type) {
            case "ADD":
                eat("ADD");
                BasicExp();
                break;

            case "MUL":
                eat("MUL");
                BasicExp();
                break;

            case "SUB":
                eat("SUB");
                BasicExp();
                break;

            case "DIV":
                eat("DIV");
                BasicExp();
                break;

            case "EQUALSEQUALS":
                eat("EQUALSEQUALS");
                BasicExp();
                break;

            case "LESSTHAN":
                eat("LESSTHAN");
                BasicExp();
                break;

            case "LESSTHANOREQUALSTO":
                eat("LESSTHANOREQUALSTO");
                BasicExp();
                break;

            default:

        }
    }

    /**
     * Check the head token and, if it matches, advance to the next token.
     *
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
}
