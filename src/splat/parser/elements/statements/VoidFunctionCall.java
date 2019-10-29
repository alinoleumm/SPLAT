package splat.parser.elements.statements;

import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.Statement;

import java.util.List;

public class VoidFunctionCall extends Statement {

    private String label;
    private List<Expression> args;

    public VoidFunctionCall(Token tok, String label, List<Expression> args) {
        super(tok);
        this.label = label;
        this.args = args;
    }

    public String getLabel() {
        return label;
    }

    public List<Expression> getArgs() {
        return args;
    }

    public String toString() {
        return label + " ( " + args.toString() + " )";
    }
}
