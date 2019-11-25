package splat.parser.elements.literals;

import com.sun.org.apache.xpath.internal.operations.Bool;
import splat.executor.Value;
import splat.executor.values.BooleanValue;
import splat.lexer.Token;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDecl;
import splat.parser.elements.declarations.RectypeDecl;
import splat.parser.elements.other.Literal;
import splat.parser.elements.types.BooleanType;

import java.util.Map;

public class BoolLiteral extends Literal {

    private boolean boolLiteral;

    public BoolLiteral(Token tok, boolean boolLiteral) {
        super(tok);
        this.boolLiteral = boolLiteral;
    }

    public boolean isBoolLiteral() {
        return boolLiteral;
    }

    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Type> varAndParamMap) {
        return new BooleanType(this.getToken());
    }

    public Value evaluate(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Value> varAndParamMap) {
        return new BooleanValue(boolLiteral);
    }

    public String toString() {
        return String.valueOf(boolLiteral);
    }
}
