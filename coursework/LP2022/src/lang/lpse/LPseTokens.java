package lang.lpse;

public final class LPseTokens {

    public static final String[][] DEFS = {
            // define your LPse tokens here
            {"BEGIN", "begin"},
            {"END", "end"},
            {"PRINTINT", "printint"},
            {"PRINTCHAR", "printchar"},
            {"SEMIC", ";"},
            {"WHILE", "while"},
            {"LBR", "\\("},
            {"RBR", "\\)"},
            {"DO", "do"},
            {"DONE", "done"},
            {"IF", "if"},
            {"THEN", "then"},
            {"ELSE", "else"},
            {"ENDIF", "endif"},
            {"INT", "-?[0-9]+"},
            {"ID", "(_[0-9a-zA-Z_$]+|[0-9a-zA-Z$][0-9a-zA-Z_$]*)"},
            {"EQUALS", "="},
            {"ADD", "\\+"},
            {"MUL", "\\*"},
            {"SUB", "\\-"},
            {"DIV", "\\/"},
            {"EQUALSEQUALS", "\\=="},
            {"LESSTHAN", "\\<"},
            {"LESSTHANOREQUALSTO", "\\<="},
    };
}
