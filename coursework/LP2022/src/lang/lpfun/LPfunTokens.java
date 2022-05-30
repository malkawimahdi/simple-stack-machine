package lang.lpfun;

public final class LPfunTokens {

    public static final String[][] DEFS = {
            // define your LPfun tokens here
            {"FUN", "fun"},
            {"BEGIN", "begin"},
            {"END", "end"},
            {"RETURN", "return"},
            {"PRINTINT", "printint"},
            {"PRINTCHAR", "printchar"},
            {"EQUALS", "\\="},
            {"COMMA", "\\,"},
            {"SEMIC", ";"},
            {"WHILE", "while"},
            {"LBR", "\\("},
            {"LCBR", "\\{"},
            {"RBR", "\\)"},
            {"RCBR", "\\}"},
            {"DO", "do"},
            {"DONE", "done"},
            {"IF", "if"},
            {"THEN", "then"},
            {"ELSE", "else"},
            {"ENDIF", "endif"},
            {"INT", "-?[0-9]+"},
            {"ID", "(_[0-9a-zA-Z_$]+|[0-9a-zA-Z$][0-9a-zA-Z_$]*)"},
            {"ADD", "\\+"},
            {"MUL", "\\*"},
            {"SUB", "\\-"},
            {"DIV", "\\/"},
            {"EQUALSEQUALS", "\\=="},
            {"LESSTHAN", "\\<"},
            {"LESSTHANOREQUALSTO", "\\<="},
    };
}
