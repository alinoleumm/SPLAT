package splat.parser.elements.literals;

import splat.lexer.Token;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDecl;
import splat.parser.elements.declarations.RectypeDecl;
import splat.parser.elements.other.Literal;

import java.util.Map;

public class StringLiteral extends Literal {

    private String stringLiteral;

    public StringLiteral(Token tok, String stringLiteral) {
        super(tok);
        this.stringLiteral = stringLiteral;
    }

    public String getStringLiteral() {
        return stringLiteral;
    }

    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Type> varAndParamMap) {
        return null;
    }

    public String toString() {
        return stringLiteral;
    }
}
