package splat.parser.elements.accesses;

import splat.lexer.Token;
import splat.parser.elements.LabelAccess;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDecl;
import splat.parser.elements.declarations.RectypeDecl;

import java.util.Map;

public class VariableOrParameter extends LabelAccess {

    private String label;

    public VariableOrParameter(Token tok, String label) {
        super(tok);
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Type> varAndParamMap) {
        return null;
    }

    public String toString() {
        return label;
    }

}
