package splat.parser.elements.expressions;

import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDecl;
import splat.parser.elements.declarations.RectypeDecl;
import splat.parser.elements.other.Literal;

import java.util.Map;

public class SingleLiteral extends Expression {

    private Literal literal;

    public SingleLiteral(Token tok, Literal literal) {
        super(tok);
        this.literal = literal;
    }

    public Literal getLiteral() {
        return literal;
    }

    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Type> varAndParamMap) {
        return null;
    }

    public String toString() {
        return literal.toString();
    }
}
