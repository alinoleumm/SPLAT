package splat.parser.elements.expressions;

import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDecl;
import splat.parser.elements.declarations.RectypeDecl;

import java.util.Map;

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

    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Type> varAndParamMap) {
        return null;
    }

    public String toString() {
        return "( " + unaryOp + " " + expr.toString() + " )";
    }
}