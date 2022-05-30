package lang.lpse;

import lang.ParseException;
import lex.Lexer;
import stackmachine.machine.SysCall;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

/*
The language is untyped.

Some remarks on the semantics:

    all variables are implicitly global

    global variables are implicitly initialised to 0

    if and while treat 0 as false and everything else as true

    the comparison operators evaluate to either 0 (false) or 1 (true)

    there are no logical operators (but they can be simulated with arithmetic)
 */

public class LPseCompiler {

    private final PrintStream out;
    //private ArrayList<String> variables = new ArrayList();
    Set<String> variables = new HashSet<String>();
    private Lexer lex;
    private int freshNameCounter;
    private int freshNameWhileCounter;


    public LPseCompiler(PrintStream out) {
        this.out = out;
    }

    public static void main(String[] args) throws IOException {
        String inFilePath = args[0];
        if (args.length > 1) {
            try (PrintStream out = new PrintStream(new FileOutputStream(args[1]))) {
                LPseCompiler compiler = new LPseCompiler(out);
                compiler.compile(inFilePath);
            }
        } else {
            LPseCompiler compiler = new LPseCompiler(System.out);
            compiler.compile(inFilePath);
        }
    }

    private String freshName(String prefix) {
        return "$" + prefix + "_" + (freshNameCounter++);
    }

    private String freshNameWhile(String prefix) {
        return "$" + prefix + "_" + (freshNameWhileCounter++);
    }

    private void emit(String s) {
        out.println(s);
    }

    public void compile(String filePath) throws IOException {
        freshNameCounter = 0;
        freshNameWhileCounter = 0;
        lex = new Lexer(LPseTokens.DEFS);
        lex.readFile(filePath);
        lex.next();
        Prog();
        emit(".data");
        for (String item : variables) {
            emit(item + ": 0");
        }
    }

    private void Prog() {
        eat("BEGIN");
        while (lex.tok().type != "END") {
            Stm();
        }
        eat("END");
        emit("halt");
        eof();
    }

    public void Stm() {
        switch (lex.tok().type) {
            case "PRINTINT":
                lex.next();
                Exp();
                eat("SEMIC");
                emit("push " + SysCall.OUT_DEC);
                emit("sysc");
                break;

            case "PRINTCHAR":
                lex.next();
                Exp();
                eat("SEMIC");
                emit("push " + SysCall.OUT_CHAR);
                emit("sysc");
                break;

            case "WHILE":
                String whileLoopLabel = freshNameWhile("while_loop");
                String whileEndLabel = freshNameWhile("while_end");
                lex.next();
                emit(whileLoopLabel + ":");
                eat("LBR");
                Exp();
                eat("RBR");
                emit("push " + whileEndLabel);
                emit("jump_z");
                eat("DO");

                while (lex.tok().type != "DONE") {
                    Stm();
                }
                emit("push " + whileLoopLabel);
                emit("jump");
                emit(whileEndLabel + ":");
                eat("DONE");
                break;

            case "IF":
                lex.next();
                String ifFalseLabel = freshName("if_false");
                String ifEndLabel = freshName("if_end");
                eat("LBR");
                Exp();
                emit("push " + ifFalseLabel);
                emit("jump_z");
                eat("RBR");
                eat("THEN");
                while (lex.tok().type != "ELSE") {
                    Stm();
                }
                emit("push " + ifEndLabel);
                emit("jump");
                eat("ELSE");
                emit(ifFalseLabel + ":");
                while (lex.tok().type != "ENDIF") {
                    Stm();
                }
                eat("ENDIF");
                emit(ifEndLabel + ":");
                break;

            case "ID":
                String ID = eat("ID");
                variables.add(ID);
                eat("EQUALS");
                Exp();
                emit("push " + ID);
                emit("store");
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
                String INT = eat("INT");
                emit("push " + INT);
                break;

            case "ID":
                String ID = eat("ID");
                emit("push " + ID);
                emit("load");
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
                emit("add");
                //emit("push " + eat("INT"));
                break;

            case "MUL":
                eat("MUL");
                BasicExp();
                emit("mul");
                break;

            case "SUB":
                eat("SUB");
                BasicExp();
                emit("sub");
                break;

            case "DIV":
                eat("DIV");
                BasicExp();
                emit("div");
                break;

            case "EQUALSEQUALS":
                eat("EQUALSEQUALS");
                BasicExp();
                emit("sub");
                emit("test_z");
                break;

            case "LESSTHAN":
                eat("LESSTHAN");
                BasicExp();
                emit("sub");
                emit("test_n");
                break;

            case "LESSTHANOREQUALSTO":
                eat("LESSTHANOREQUALSTO");
                BasicExp();
                emit("push 1");
                emit("test_n");
                break;

            default:
        }
    }

    /**
     * Verify that the token stream is at EOF.
     *
     * @throws ParseException if token stream is not at EOF
     */
    private void eof() {
        if (lex.tok().type != "EOF") {
            throw new ParseException(lex.tok(), "EOF");
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