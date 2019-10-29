package splat.parser.elements.literals;

import splat.lexer.Token;
import splat.parser.elements.other.Literal;

public class StringLiteral extends Literal {

    private String stringLiteral;

    public StringLiteral(Token tok, String stringLiteral) {
        super(tok);
        this.stringLiteral = stringLiteral;
    }

    public String getStringLiteral() {
        return stringLiteral;
    }

    public String toString() {
        return stringLiteral;
    }
}
