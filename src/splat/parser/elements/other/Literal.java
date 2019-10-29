package splat.parser.elements.other;

import splat.lexer.Token;
import splat.parser.elements.Expression;

public abstract class Literal extends Expression {
    public Literal(Token tok) {
        super(tok);
    }
}
