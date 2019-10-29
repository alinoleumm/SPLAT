package splat.parser.elements.accesses;

import splat.lexer.Token;
import splat.parser.elements.LabelAccess;

public class VariableOrParameter extends LabelAccess {

    private String label;

    public VariableOrParameter(Token tok, String label) {
        super(tok);
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String toString() {
        return label;
    }

}
