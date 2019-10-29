package splat.parser.elements.expressions;

import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.LabelAccess;

public class SingleLabelAccess extends Expression {

    private LabelAccess labelAccess;

    public SingleLabelAccess(Token tok, LabelAccess labelAccess) {
        super(tok);
        this.labelAccess = labelAccess;
    }

    public LabelAccess getLabelAccess() {
        return labelAccess;
    }

    public String toString() {
        return labelAccess.toString();
    }

}
