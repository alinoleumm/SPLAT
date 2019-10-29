package splat.parser.elements.statements;

import splat.lexer.Token;
import splat.parser.elements.Statement;

public class PrintLine extends Statement {

    public PrintLine(Token tok) {
        super(tok);
    }

    public String toString() {
        return "print_line";
    }
}
