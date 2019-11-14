package splat.parser.elements.literals;

import splat.lexer.Token;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDecl;
import splat.parser.elements.declarations.RectypeDecl;
import splat.parser.elements.other.Literal;
import splat.parser.elements.types.RecType;

import java.util.Map;

public class RectypeLiteral extends Literal {

    public RectypeLiteral(Token tok) {
        super(tok);
    }

    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Type> varAndParamMap) {
        return new RecType(this.getToken(), "null");
    }

    public String toString() {
        return "null";
    }

}
