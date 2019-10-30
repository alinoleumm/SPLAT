package splat.parser.elements.vartypes;

import splat.lexer.Token;
import splat.parser.elements.VarType;

public class StringVarType extends VarType {
    public StringVarType(Token tok) {
        super(tok);
    }

    public String toString() {
        return "String";
    }

}
