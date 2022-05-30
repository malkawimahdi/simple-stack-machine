package teeny;

import lex.Lexer;

/**
 * Grammar for a teeny-weeny language.
 * ===================================
 *
 * Prog         -> BEGIN Stm* END
 *
 * Stm          -> IF LBR Exp RBR Stm ELSE Stm
 * Stm          -> PRINTINT Exp SEMIC
 * Stm          -> PRINTCHAR Exp SEMIC
 *
 * Exp          -> INT
 *
 */

public class TeenyWeenyParser {

    private Lexer lex;

    //Token definitions which the lexical analyser will use to tokenize input stream.
    private String[][] tokenDefs = {
        { "LBR",         "\\(" },
        { "RBR",         "\\)" },
        { "BEGIN",       "begin" },
        { "END",         "end" },
        { "PRINTINT",    "printint" },
        { "PRINTCHAR",   "printchar" },
        { "IF",          "if" },
        { "ELSE",        "else" },
        { "SEMIC",       ";" },
        {"INT",          "-?[0-9]+"},
        {"LCBR",          "\\{" },
        {"RCBR",          "\\}" },
        // add additional token definitions as required
    };

    public TeenyWeenyParser() {
    }

    //Creates a new lexical analyser with tokens defined above.
    public void parse(String filePath) throws Exception {
        lex = new Lexer(tokenDefs);
        lex.readFile(filePath);
        lex.next();
        //Calls prog() function in EVERY program written in this language since this is a Recursive Decent Parser.
        Prog();
        System.out.println("Parse succeeded.");
    }

    //Eats the BEGIN token which will consume current token and go to the next token.
    public void Prog() throws Exception {
        // Prog -> BEGIN Stm* END
        eat("BEGIN");
        while (lex.tok().type != "END") {
            Stm();
        }
        eat("END");

        // add a test to make sure that there is no "junk" at
        // the end (test for EOF)
    }


    //Switch statement based on input token to determine what the parser will need to process.
    public void Stm() throws Exception {
        switch (lex.tok().type){

            case "IF":
                lex.next(); eat("LBR"); Exp(); eat("RBR"); Stm(); eat("ELSE"); Stm();
                break;

            case "PRINTINT":
                lex.next(); Exp(); eat("SEMIC");
                break;

            case "PRINTCHAR":
                lex.next(); Exp(); eat("SEMIC");
                break;

            case "LCBR":
                lex.next();
                while (lex.tok().type != "RCBR") {
                    Stm();
                }
                eat("RCBR");
                break;

            default:
                throw new Exception("Token Not Recognised");

        }
    }


    public void Exp(){
        System.out.println(lex.tok());
        eat("INT");
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

    public static void main(String[] args) throws Exception {
        TeenyWeenyParser parser = new TeenyWeenyParser();
        parser.parse(args[0]);
    }
}
