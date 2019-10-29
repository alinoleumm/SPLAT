package splat.parser.elements.other;

import splat.parser.elements.Type;

public class FieldDeclaration {

    private String label;
    private Type type;

    public FieldDeclaration(String label, Type type) {
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
