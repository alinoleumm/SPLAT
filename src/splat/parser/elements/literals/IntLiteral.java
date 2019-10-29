package splat.parser.elements.literals;

import splat.lexer.Token;
import splat.parser.elements.other.Literal;

public class IntLiteral extends Literal {

    private int intLiteral;

    public IntLiteral(Token tok, int intLiteral) {
        super(tok);
        this.intLiteral = intLiteral;
    }

    public int getIntLiteral() {
        return intLiteral;
    }

    public String toString() {
        return String.valueOf(intLiteral);
    }
}
