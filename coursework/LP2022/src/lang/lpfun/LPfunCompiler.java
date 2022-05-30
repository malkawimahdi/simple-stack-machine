package lang.lpfun;

import lang.ParseException;
import lex.Lexer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/*
The language is untyped.

Some remarks on the semantics:

    any variable which is not a formal parameter is implicitly global

    lexical scoping applies when a formal parameter name clashes with a global
    (ie a local is permitted to create a hole in the scope of a global)

    global variables are implicitly initialised to 0

    if and while treat 0 as false and everything else as true

    the comparison operators evaluate to either 0 (false) or 1 (true)

    there are no logical operators (but they can be simulated with arithmetic)

    behaviour of calling a fun with the wrong number of parameters is undefined (of course, this should be caught by static analysis, but the crude structure of this compiler does not facilitate that)

    if a function execution reaches the end of the function body without executing a return statement, the function will return normally, with a return value of 0

    return from within the begin ... end block exits the program (the return value is ignored in this case)
 */

public class LPfunCompiler {

    private Lexer lex;
    private int freshNameCounter;
    private PrintStream out;

    public LPfunCompiler(PrintStream out) {
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
        lex = new Lexer(LPfunTokens.DEFS);
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
                LPfunCompiler compiler = new LPfunCompiler(out);
                compiler.compile(inFilePath);
            }
        } else {
            LPfunCompiler compiler = new LPfunCompiler(System.out);
            compiler.compile(inFilePath);
        }
    }
}
