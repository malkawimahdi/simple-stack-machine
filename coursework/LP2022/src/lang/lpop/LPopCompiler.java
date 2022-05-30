package lang.lpop;

import lang.ParseException;
import lex.Lexer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/*
The language is untyped.

Some remarks on the semantics:

    all variables are implicitly global

    global variables are implicitly initialised to 0

    if and while treat 0 as false and everything else as true

    operator precedence is (==, <, <=) lowest, then (+, -), then (*, /);
    operators of equal precedence associate to the left

    the comparison operators evaluate to either 0 (false) or 1 (true)

    there are no logical operators (but they can be simulated with arithmetic)
 */

public class LPopCompiler {

    private Lexer lex;
    private int freshNameCounter;
    private PrintStream out;

    public LPopCompiler(PrintStream out) {
        this.out = out;
    }

    private String freshName(String prefix) {
        return "$" + prefix + "_" + (freshNameCounter++);
    }

    private void emit(String s) {
        out.println(s);
    }

    public void compile(String filePath) throws IOException {
        freshNameCounter = 0;
        lex = new Lexer(LPopTokens.DEFS);
        lex.readFile(filePath);
        lex.next();
        // to be completed
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
        String inFilePath = args[0];
        if (args.length > 1) {
            try (PrintStream out = new PrintStream(new FileOutputStream(args[1]))) {
                LPopCompiler compiler = new LPopCompiler(out);
                compiler.compile(inFilePath);
            }
        } else {
            LPopCompiler compiler = new LPopCompiler(System.out);
            compiler.compile(inFilePath);
        }
    }
}
