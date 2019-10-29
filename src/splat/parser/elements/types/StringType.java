package splat.parser.elements.types;

import splat.lexer.Token;
import splat.parser.elements.Type;

public class StringType extends Type {
    public StringType(Token tok) {
        super(tok);
    }

    public String toString() {
        return "String";
    }
}
