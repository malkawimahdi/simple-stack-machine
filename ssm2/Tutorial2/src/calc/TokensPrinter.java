package calc;

//Proprietary Java Import
import lex.Lexer;

//Java module imported
import java.io.IOException;

//Creating a public class called TokensPrinter
public class TokensPrinter {

    /* Private static final String Array which will have 2 elements. This structure, is an array of an array, where
     * an array of the language definition is stored. Then all of these arrays of the language definition are stored in
     * an array which holds all of these arrays in the large array called tokenDefs. */
    private static final String[][] tokenDefs = {
            {"PLUS", "\\+"},
            {"INT", "-?[0-9]+"},
            {"MINUS", "-"},
            {"TIMES", "\\*"},
            {"EQUALS", "="},
    };

    static Lexer lex;  //Builds a lexer.

    //Main method
    public static void main(String[] args) throws IOException {
        lex = new Lexer(tokenDefs); /*The lexical analyser will take the tokenDefs array that was created above to
        create a specification out of the alphabet that has been defined above. */

        lex.readFile(args[0]); //The lex will read the file that is given as the first argument.

        lex.next(); //The lexer will build the first token in the file.
        // Note: this code assumes that the input file contains
        // at least one token. This gives you a code structure
        // which is easier to adapt when building your Calculator
        // (all valid calc programs start with an INT token).
        // But what happens if you run TokensPrinter on an empty file?
        System.out.println("First token: " + lex.tok()); //Output the first token which will also be the current token.

        lex.next(); //The lexer will proceed to look at the next possible token.

        while (lex.tok().type != "EOF") { /*Remove ".type" to get more information, so you can see the match. The lexer
        will continue to process tokens until the special case token "EOF" is located which is an end-of-file token.*/

            System.out.print("Found token: " + lex.tok()); //Output for each token, where it will process the token.

            System.out.println(" on line " + lex.tok().lineNumber); //Output the line number that the token is located.

            lex.next(); //Builds the next token that is located.
        }
    }
}
