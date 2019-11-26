package splat.parser.elements.vartypes;

import splat.executor.Value;
import splat.executor.values.BooleanValue;
import splat.lexer.Token;
import splat.parser.elements.VarType;

public class BooleanVarType extends VarType {
    public BooleanVarType(Token tok) {
        super(tok);
    }

    public Value getInitialValue() {
        return new BooleanValue(false);
    }

    public String toString() {
        return "Boolean";
    }
}
