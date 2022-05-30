package lang.loopy;

import lang.ParseException;
import lex.Lexer;

import java.io.IOException;

/**
 Stm     -> OUTPUT Exp SEMIC
 Stm     -> LCBR StmList RCBR
 Stm     -> LoopStm

 LoopStm -> REPEAT StmList UNTIL LBR Exp RBR
 LoopStm -> WHILE Exp Stm

 StmList -> Stm StmList
 StmList ->

 Exp     -> INT
 *
 */

public class LoopyParser {

    private Lexer lex;

    public LoopyParser() {
    }

    public void parse(String filePath) throws IOException {
        lex = new Lexer(LoopyTokens.DEFS);
        lex.readFile(filePath);
        lex.next();
        //Colours();
        eof(); // the parse must fail if there is any "junk" at the end of the input file
        System.out.println("Parse succeeded.");
    }

    public void LoopStm(){

        switch (lex.tok().type){
            case "REPEAT":
                StmList();
                eat("UNTIL");
                eat("LBR");
                Exp();
                eat("RBR");
                break;


            case "WHILE":
                Exp();
                Stm();
                break;
        }
    }

    public void Stm(){
        switch (lex.tok().type){
            case "OUTPUT":
                Exp();
                eat("SEMIC");
                lex.next();
                break;

            case "LCBR":
                StmList();
                eat("RCBR");
                lex.next();
                break;

            default:
                LoopStm();
                lex.next();
        }
    }


   public void StmList(){
        switch(lex.tok().type){
            case "OUTPUT", "LCBR", "REPEAT", "WHILE":
                Stm();
                StmList();
                break;

            default:
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

    public void Exp(){
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

    public static void main(String[] args) throws IOException {
        LoopyParser parser = new LoopyParser();
        parser.parse(args[0]);
    }
}
