package splat.parser.elements.literals;

import splat.lexer.Token;
import splat.parser.elements.other.Literal;

public class BoolLiteral extends Literal {

    private boolean boolLiteral;

    public BoolLiteral(Token tok, boolean boolLiteral) {
        super(tok);
        this.boolLiteral = boolLiteral;
    }

    public boolean isBoolLiteral() {
        return boolLiteral;
    }

    public String toString() {
        return String.valueOf(boolLiteral);
    }
}
