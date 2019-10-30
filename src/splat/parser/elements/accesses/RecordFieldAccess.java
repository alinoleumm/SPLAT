package splat.parser.elements.accesses;

import splat.lexer.Token;
import splat.parser.elements.LabelAccess;

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

    public String toString() {
        return labelAccess.toString() + " . " + field;
    }

}
