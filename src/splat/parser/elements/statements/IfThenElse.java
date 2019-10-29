package splat.parser.elements.statements;

import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.Statement;

import java.util.List;

public class IfThenElse extends Statement {

    private Expression expr;
    private List<Statement> stmtsTrue;
    private List<Statement> stmtsFalse;

    public IfThenElse(Token tok, Expression expr, List<Statement> stmtsTrue, List<Statement> stmtsFalse) {
        super(tok);
        this.expr = expr;
        this.stmtsTrue = stmtsTrue;
        this.stmtsFalse = stmtsFalse;
    }

    public Expression getExpr() {
        return expr;
    }

    public List<Statement> getStmtsTrue() {
        return stmtsTrue;
    }

    public List<Statement> getStmtsFalse() {
        return stmtsFalse;
    }

    public String toString() {
        return "if " + expr.toString() + " then " + stmtsTrue.toString() + " else " + stmtsFalse.toString() + " end if";
    }
}
