package calc;

//Proprietary

import lex.Lexer;

//Java module imported
import java.io.IOException;

//Creating a public class called TokensPrinter
public class Compiler {

    /* Private static final String Array which will have 2 elements. This structure, is an array of an array, where
     * an array of the language definition is stored. Then all of these arrays of the language definition are stored in
     * an array which holds all of these arrays in the large array called tokenDefs. */
    private static final String[][] tokenDefs = {
            {"PLUS", "\\+"},
            {"INT", "-?[0-9]+"},
            {"MINUS","-"},
            {"TIMES", "\\*"},
            {"EQUALS", "="},
            // define your tokens here.
            // I've done one for you. You need to add definitions for
            // INT, MINUS, TIMES and EQUALS
    };

    static Lexer lex;  //Builds a lexer.

    //Main method
    public static void main(String[] args) throws Exception {
        lex = new Lexer(tokenDefs);
        lex.readFile(args[0]);

        int running_sum = 0;
        lex.next();


        while (lex.tok().type != "EOF") {

            if (lex.tok().type == "PLUS") {
                System.out.print("+");
            } else if (lex.tok().type == "EQUALS") {
                System.out.println("push runningSum");
                System.out.println("push load");
                System.out.println("push 3");
                System.out.println("push sysc");
                System.out.println("halt");
                lex.next();
            } else if (lex.tok().type == "INT") {
                int i = Integer.parseInt(lex.tok().image);
                System.out.println("push runningSum");
                System.out.println("load");
                System.out.println("push" + i);
                System.out.println("add");
            }


            //Problem was, that I was attempting to use Integer.parseInt on the '-' symbol, which resulted in the error.
            else if (lex.tok().type == "MINUS") {
                lex.next();
                if (lex.tok().type == "INT") {
                    int i = Integer.parseInt(lex.tok().image);
                    System.out.println("push runningSum");
                    System.out.println("load");
                    System.out.println("push" + i);
                    System.out.println("sub");
                } else {
                    throw new Exception("Input character is not valid.");
                }
            } else if (lex.tok().type == "TIMES") {
                lex.next();
                if (lex.tok().type == "INT") {
                    int i = Integer.parseInt(lex.tok().image);
                    System.out.println("$loop");
                    System.out.println("push x");
                    System.out.println("load");
                    System.out.println("push $end");
                    System.out.println("jump_z");
                    System.out.println("push x");
                    System.out.println("load");
                    System.out.println("push 1");
                    System.out.println("sub");
                    System.out.println("push x");
                    System.out.println("store");
                    System.out.println("push y");
                    System.out.println("load");
                    System.out.println("push addition");
                    System.out.println("load");
                    System.out.println("add");
                    System.out.println("push $loop");
                    System.out.println("jump");
                    System.out.println("$end");
                    System.out.println("push addition");
                    System.out.println("load");
                    System.out.println("push 3");
                    System.out.println("sysc");
                    System.out.println("halt");
                } else {
                    throw new Exception("Input character is not valid.");
                }
            }

        }
    }
}






