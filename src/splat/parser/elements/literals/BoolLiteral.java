package splat.parser.elements.literals;

import splat.lexer.Token;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDecl;
import splat.parser.elements.declarations.RectypeDecl;
import splat.parser.elements.other.Literal;

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
        return null;
    }

    public String toString() {
        return String.valueOf(boolLiteral);
    }
}
