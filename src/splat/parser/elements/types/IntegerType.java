package splat.parser.elements.types;

import splat.lexer.Token;
import splat.parser.elements.Type;

public class IntegerType extends Type {
    public IntegerType(Token tok) {
        super(tok);
    }

    public String toString() {
        return "Integer";
    }
}
