package splat.parser.elements.accesses;

import splat.lexer.Token;
import splat.parser.elements.LabelAccess;

public class RecordFieldAccess extends LabelAccess {

    private LabelAccess labelAccess;
    private String label;

    public RecordFieldAccess(Token tok, LabelAccess labelAccess, String label) {
        super(tok);
        this.labelAccess = labelAccess;
        this.label = label;
    }

    public LabelAccess getLabelAccess() {
        return labelAccess;
    }

    public String getLabel() {
        return label;
    }

    public String toString() {
        return labelAccess.toString() + " . " + label;
    }

}
