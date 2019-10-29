package splat.parser.elements.other;

import splat.parser.elements.Type;

public class Parameter {

    private String label;
    private Type type;

    public Parameter(String label, Type type) {
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
