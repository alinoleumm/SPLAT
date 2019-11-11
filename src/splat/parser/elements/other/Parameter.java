package splat.parser.elements.other;

import splat.lexer.Token;
import splat.parser.elements.ASTElement;
import splat.parser.elements.Type;

public class Parameter extends ASTElement {

    private String label;
    private Type type;

    public Parameter(Token tok, String label, Type type) {
        super(tok);
        this.label = label;
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public Type getType() {
        return type;
    }

    public String toString() {
        return label + " : " + type.toString();
    }

}
