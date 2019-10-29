package splat.parser.elements.statements;

import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.Statement;

import java.util.List;

public class WhileLoop extends Statement {

    private Expression expr;
    private List<Statement> stmts;

    public WhileLoop(Token tok, Expression expr, List<Statement> stmts) {
        super(tok);
        this.expr = expr;
        this.stmts = stmts;
    }

    public Expression getExpr() {
        return expr;
    }

    public List<Statement> getStmts() {
        return stmts;
    }

    public String toString() {
        return "while " + expr.toString() + " do " + stmts.toString() + " end while";
    }

}
