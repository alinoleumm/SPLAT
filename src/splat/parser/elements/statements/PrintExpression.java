package splat.parser.elements.statements;

import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.Statement;

public class PrintExpression extends Statement {

    private Expression expr;

    public PrintExpression(Token tok, Expression expr) {
        super(tok);
        this.expr = expr;
    }

    public Expression getExpr() {
        return expr;
    }

    public String toString() {
        return "print " + expr.toString();
    }

}
