package splat.parser.elements.statements;

import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.Statement;

import java.util.List;

public class IfThen extends Statement {

    private Expression expr;
    private List<Statement> stmtsTrue;

    public IfThen(Token tok, Expression expr, List<Statement> stmtsTrue) {
        super(tok);
        this.expr = expr;
        this.stmtsTrue = stmtsTrue;
    }

    public Expression getExpr() {
        return expr;
    }

    public List<Statement> getStmtsTrue() {
        return stmtsTrue;
    }

    public String toString() {
        return "if " + expr.toString() + " then " + stmtsTrue.toString() + " end if";
    }

}
