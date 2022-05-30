package lang.lpfun;

import lang.ParseException;
import lex.Lexer;

public class LPfunParser {

    private Lexer lex;

    public LPfunParser() {
    }

    public static void main(String[] args) throws Exception {
        LPfunParser parser = new LPfunParser();
        parser.parse(args[0]);
    }

    public void parse(String filePath) throws Exception {
        lex = new Lexer(LPfunTokens.DEFS);
        lex.readFile(filePath);
        lex.next();
        Prog();
        System.out.println("Parse succeeded.");
        eof();
    }

    public void Prog() {
        eat("BEGIN");

        while (lex.tok().type != "END") {
            Stm();
        }

        eat("END");
        while (lex.tok().type != "EOF") {
            DefineFunction();
        }
    }

    private void Stm() {
        switch (lex.tok().type) {
            case "PRINTINT":
                eat("PRINTINT");
                Exp();
                eat("SEMIC");
                break;

            case "PRINTCHAR":
                eat("PRINTCHAR");
                Exp();
                eat("SEMIC");
                break;

            case "WHILE":
                eat("WHILE");
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
                eat("IF");
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
                eat("ID");
                IDRest();
                eat("SEMIC");
                break;

            case "RETURN":
                eat("RETURN");
                Exp();
                eat("SEMIC");
                break;

        }
    }

    private void DefineFunction() {
        switch (lex.tok().type) {
            case "FUN":
                eat("FUN");
                eat("ID");
                eat("LBR");
                Exp();
                eat("RBR");
                eat("LCBR");

                while (lex.tok().type != "RCBR") {
                    Stm();
                }
                eat("RCBR");
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
                IDRest();
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
                Exp();
                break;

            case "MUL":
                eat("MUL");
                Exp();
                break;

            case "SUB":
                eat("SUB");
                Exp();
                break;

            case "DIV":
                eat("DIV");
                Exp();
                break;

            case "EQUALSEQUALS":
                eat("EQUALSEQUALS");
                Exp();
                break;

            case "LESSTHAN":
                eat("LESSTHAN");
                Exp();
                break;

            case "LESSTHANOREQUALSTO":
                eat("LESSTHANOREQUALSTO");
                Exp();
                break;

            case "COMMA":
                eat("COMMA");
                Exp();
                break;

            default:

        }
    }


    private void IDRest() {
        switch (lex.tok().type) {

            case "LBR":
                Exp();
                break;

            case "EQUALS":
                eat("EQUALS");
                Exp();
                break;
        }
    }

    /**
     * Check the head token and, if it matches, advance to the next token.
     *
     * @return the text of the head token that was matched
     * @throws ParseException if the head token does not match.
     */

    private void eof() {
        if (lex.tok().type != "EOF") {
            throw new ParseException(lex.tok(), "EOF");
        }
    }

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
