package splat.parser.elements.expressions;

import splat.lexer.Token;
import splat.parser.elements.Expression;

import java.util.List;

public class NonVoidFunctionCall extends Expression {

    private String label;
    private List<Expression> args;

    public NonVoidFunctionCall(Token tok, String label, List<Expression> args) {
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
        return label  + " ( " + args.toString() + " )";
    }

}
