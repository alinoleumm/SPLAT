package splat.parser.elements.vartypes;

import splat.lexer.Token;
import splat.parser.elements.VarType;

public class BooleanVarType extends VarType {
    public BooleanVarType(Token tok) {
        super(tok);
    }
    public String toString() {
        return "Boolean";
    }
}
