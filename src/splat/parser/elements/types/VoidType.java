package splat.parser.elements.types;

import splat.lexer.Token;
import splat.parser.elements.Type;

public class VoidType extends Type {

    public VoidType(Token tok) {
        super(tok);
    }

    public String toString() {
        return "null";
    }

}
