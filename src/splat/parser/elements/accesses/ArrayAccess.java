package splat.parser.elements.accesses;

import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.LabelAccess;

public class ArrayAccess extends LabelAccess {

    private LabelAccess labelAccess;
    private Expression expr;

    public ArrayAccess(Token tok, LabelAccess labelAccess, Expression expr) {
        super(tok);
        this.labelAccess = labelAccess;
        this.expr = expr;
    }

    public LabelAccess getLabelAccess() {
        return labelAccess;
    }

    public Expression getExpr() {
        return expr;
    }

    public String toString() {
        return labelAccess.toString() + " [ " + expr.toString() + " ]";
    }

}
