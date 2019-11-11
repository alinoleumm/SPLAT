package splat.parser.elements.accesses;

import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.LabelAccess;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDecl;
import splat.parser.elements.declarations.RectypeDecl;

import java.util.Map;

public class ArrayAccess extends LabelAccess {

    private LabelAccess labelAccess;
    private Expression expr;

    public ArrayAccess(Token tok, LabelAccess labelAccess, Expression expr) {
        super(tok);
        this.labelAccess = labelAccess;
        this.expr = expr;
    }

    public LabelAccess getLabelAccess() {
        return labelAccess;
    }

    public Expression getExpr() {
        return expr;
    }

    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Type> varAndParamMap) {
        return null;
    }

    public String toString() {
        return labelAccess.toString() + " [ " + expr.toString() + " ]";
    }
}
