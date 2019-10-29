package splat.parser.elements.expressions;

import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.other.Literal;

public class SingleLiteral extends Expression {

    private Literal literal;

    public SingleLiteral(Token tok, Literal literal) {
        super(tok);
        this.literal = literal;
    }

    public Literal getLiteral() {
        return literal;
    }

    public String toString() {
        return literal.toString();
    }
}
