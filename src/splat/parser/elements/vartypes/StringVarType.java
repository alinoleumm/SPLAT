package splat.parser.elements.vartypes;

import splat.executor.Value;
import splat.executor.values.StringValue;
import splat.lexer.Token;
import splat.parser.elements.VarType;

public class StringVarType extends VarType {
    public StringVarType(Token tok) {
        super(tok);
    }

    public Value getInitialValue() {
        return new StringValue("");
    }

    public String toString() {
        return "String";
    }

}
