package splat.parser.elements.vartypes;

import splat.lexer.Token;
import splat.parser.elements.VarType;

public class IntegerVarType extends VarType {
    public IntegerVarType(Token tok) {
        super(tok);
    }

    public String toString() {
        return "Integer";
    }
}
