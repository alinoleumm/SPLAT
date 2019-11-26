package splat.parser.elements.vartypes;

import splat.executor.Value;
import splat.lexer.Token;
import splat.parser.elements.VarType;

public class RecVarType extends VarType {

    private String label;

    public RecVarType(Token tok, String label) {
        super(tok);
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String toString() {
        return label;
    }

    public Value getInitialValue() {
        return null;
    }
}
