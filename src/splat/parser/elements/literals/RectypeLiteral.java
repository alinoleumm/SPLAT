package splat.parser.elements.literals;

import splat.lexer.Token;
import splat.parser.elements.other.Literal;

public class RectypeLiteral extends Literal {

    public RectypeLiteral(Token tok) {
        super(tok);
    }

    @Override
    public String toString() {
        return "null";
    }

}
