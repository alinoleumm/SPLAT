package splat.parser.elements.expressions;

import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.LabelAccess;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDecl;
import splat.parser.elements.declarations.RectypeDecl;

import java.util.Map;

public class SingleLabelAccess extends Expression {

    private LabelAccess labelAccess;

    public SingleLabelAccess(Token tok, LabelAccess labelAccess) {
        super(tok);
        this.labelAccess = labelAccess;
    }

    public LabelAccess getLabelAccess() {
        return labelAccess;
    }

    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Type> varAndParamMap) {
        return null;
    }

    public String toString() {
        return labelAccess.toString();
    }

}
