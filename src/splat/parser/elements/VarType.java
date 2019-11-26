package splat.parser.elements;

import splat.executor.Value;
import splat.lexer.Token;

public abstract class VarType extends Type {

    public VarType(Token tok) {
        super(tok);
    }

    abstract public Value getInitialValue();

}
