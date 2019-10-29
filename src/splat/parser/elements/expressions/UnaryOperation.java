package splat.parser.elements.expressions;

import splat.lexer.Token;
import splat.parser.elements.Expression;

public class UnaryOperation extends Expression {

    private String unaryOp;
    private Expression expr;

    public UnaryOperation(Token tok, String unaryOp, Expression expr) {
        super(tok);
        this.unaryOp = unaryOp;
        this.expr = expr;
    }

    public String getUnaryOp() {
        return unaryOp;
    }

    public Expression getExpr() {
        return expr;
    }

    public String toString() {
        return "( " + unaryOp + " " + expr.toString() + " )";
    }
}
