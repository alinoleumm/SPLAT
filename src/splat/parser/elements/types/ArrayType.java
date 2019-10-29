package splat.parser.elements.types;

import splat.lexer.Token;
import splat.parser.elements.Type;

public class ArrayType extends Type {

    private Type type;

    public ArrayType(Token tok, Type type) {
        super(tok);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public String toString() {
        return type.toString() + "[]";
    }
}
