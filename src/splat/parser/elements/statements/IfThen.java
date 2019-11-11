package splat.parser.elements.statements;

import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.Statement;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDecl;
import splat.parser.elements.declarations.RectypeDecl;

import java.util.List;
import java.util.Map;

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

    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Type> varAndParamMap) {

    }

    public String toString() {
        return "if " + expr.toString() + " then " + stmtsTrue.toString() + " end if";
    }

}
