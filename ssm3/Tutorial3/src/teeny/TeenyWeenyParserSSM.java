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

@SuppressWarnings("ALL")

//Class which will compile SSM code based on parser.
public class TeenyWeenyParserSSM {

    private Lexer lex;

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

    public TeenyWeenyParserSSM() {
    }

    public void parse(String filePath) throws Exception {
        lex = new Lexer(tokenDefs);
        lex.readFile(filePath);
        lex.next();
        Prog();
        //System.out.println("Parse succeeded.");
    }

    public void Prog() throws Exception {
        // Prog -> BEGIN Stm* END
        System.out.println("//begin");
        eat("BEGIN");

        while (lex.tok().type != "END") {
            Stm();
        }
        eat("END");
        System.out.println("halt");

        // add a test to make sure that there is no "junk" at
        // the end (test for EOF)
    }

    public void Stm() throws Exception {
        int i;
        switch (lex.tok().type){

            case "IF":
                lex.next();
                eat("LBR");
                Exp();
                System.out.println("test_z");
                System.out.println("push $false");
                System.out.println("jump_z");
                eat("RBR");

                System.out.println("push $else");
                System.out.println("jump");
                System.out.println("$false:");

                Stm();
                System.out.println("push $end");
                System.out.println("jump");
                eat("ELSE");
                System.out.println("$else: ");
                Stm();

                System.out.println("$end:");
                break;

            case "PRINTINT":
                lex.next();
                Exp();
                System.out.println("push 3");
                System.out.println("sysc");
                System.out.println("push 2");
                System.out.println("sysc");
                System.out.println(" ");
                eat("SEMIC");
                break;

            case "PRINTCHAR":
                lex.next();
                Exp();
                System.out.println("push 3");
                System.out.println("sysc");
                System.out.println("push 2");
                System.out.println("sysc");
                System.out.println(" ");
                eat("SEMIC");
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
        //System.out.println(lex.tok());
        int i;
        i = Integer.parseInt(eat("INT"));
        System.out.println("push " + i);
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
        TeenyWeenyParserSSM parser = new TeenyWeenyParserSSM();
        parser.parse(args[0]);
    }
}
