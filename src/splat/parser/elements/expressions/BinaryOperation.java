package splat.parser.elements.expressions;

import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDecl;
import splat.parser.elements.declarations.RectypeDecl;

import java.util.Map;

public class BinaryOperation extends Expression {

    private Expression exprLeft;
    private String binaryOp;
    private Expression exprRight;

    public BinaryOperation(Token tok, Expression exprLeft, String binaryOp, Expression exprRight) {
        super(tok);
        this.exprLeft = exprLeft;
        this.binaryOp = binaryOp;
        this.exprRight = exprRight;
    }

    public Expression getExprLeft() {
        return exprLeft;
    }

    public String getBinaryOp() {
        return binaryOp;
    }

    public Expression getExprRight() {
        return exprRight;
    }

    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Type> varAndParamMap) {
        return null;
    }

    public String toString() {
        return "( " + exprLeft.toString() + " " + binaryOp + " " + exprRight.toString() + " ) ";
    }

}
