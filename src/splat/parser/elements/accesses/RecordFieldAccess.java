package splat.parser.elements.accesses;

import splat.lexer.Token;
import splat.parser.elements.LabelAccess;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDecl;
import splat.parser.elements.declarations.RectypeDecl;

import java.util.Map;

public class RecordFieldAccess extends LabelAccess {

    private LabelAccess labelAccess;
    private String field;

    public RecordFieldAccess(Token tok, LabelAccess labelAccess, String field) {
        super(tok);
        this.labelAccess = labelAccess;
        this.field = field;
    }

    public LabelAccess getLabelAccess() {
        return labelAccess;
    }

    public String getField() {
        return field;
    }

    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Type> varAndParamMap) {
        return null;
    }

    public String toString() {
        return labelAccess.toString() + " . " + field;
    }

}
