package splat.parser.elements.literals;

import splat.executor.Value;
import splat.executor.values.IntegerValue;
import splat.lexer.Token;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDecl;
import splat.parser.elements.declarations.RectypeDecl;
import splat.parser.elements.other.Literal;
import splat.parser.elements.types.IntegerType;

import java.util.Map;

public class IntLiteral extends Literal {

    private int intLiteral;

    public IntLiteral(Token tok, int intLiteral) {
        super(tok);
        this.intLiteral = intLiteral;
    }

    public int getIntLiteral() {
        return intLiteral;
    }

    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Type> varAndParamMap) {
        return new IntegerType(this.getToken());
    }

    public Value evaluate(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Value> varAndParamMap) {
        return new IntegerValue(intLiteral);
    }

    public String toString() {
        return String.valueOf(intLiteral);
    }
}
