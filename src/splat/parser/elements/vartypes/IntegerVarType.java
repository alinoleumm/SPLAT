package splat.parser.elements.vartypes;

import splat.executor.Value;
import splat.executor.values.IntegerValue;
import splat.lexer.Token;
import splat.parser.elements.VarType;

public class IntegerVarType extends VarType {
    public IntegerVarType(Token tok) {
        super(tok);
    }

    public Value getInitialValue() {
        return new IntegerValue(0);
    }

    public String toString() {
        return "Integer";
    }
}
