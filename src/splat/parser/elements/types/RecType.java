package splat.parser.elements.types;

import splat.lexer.Token;
import splat.parser.elements.Type;

public class RecType extends Type {

    private String label;

    public RecType(Token tok, String label) {
        super(tok);
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String toString() {
        return label;
    }

}
