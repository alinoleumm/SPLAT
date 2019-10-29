package splat.parser.elements.statements;

import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.LabelAccess;
import splat.parser.elements.Statement;

public class Assignment extends Statement {

    private LabelAccess labelAccess;
    private Expression expr;

    public Assignment(Token tok, LabelAccess labelAccess, Expression expr) {
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
        return labelAccess.toString() + " := " + expr.toString();
    }

}
