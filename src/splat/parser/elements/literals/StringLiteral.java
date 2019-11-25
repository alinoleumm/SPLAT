package splat.parser.elements.literals;

import splat.executor.Value;
import splat.executor.values.StringValue;
import splat.lexer.Token;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDecl;
import splat.parser.elements.declarations.RectypeDecl;
import splat.parser.elements.other.Literal;
import splat.parser.elements.types.StringType;

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
        return new StringType(this.getToken());
    }

    public Value evaluate(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Value> varAndParamMap) {
        return new StringValue(stringLiteral);
    }

    public String toString() {
        return stringLiteral;
    }
}
