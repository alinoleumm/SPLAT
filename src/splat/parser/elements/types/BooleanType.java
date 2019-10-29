package splat.parser.elements.types;

import splat.lexer.Token;
import splat.parser.elements.Type;

public class BooleanType extends Type {
    public BooleanType(Token tok) {
        super(tok);
    }

    public String toString() {
        return "Boolean";
    }
}
