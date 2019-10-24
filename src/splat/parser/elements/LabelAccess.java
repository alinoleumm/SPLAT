package splat.parser.elements;

import splat.lexer.Token;

public abstract class LabelAccess extends Expression {

	public LabelAccess(Token tok) {
		super(tok);
	}
}
