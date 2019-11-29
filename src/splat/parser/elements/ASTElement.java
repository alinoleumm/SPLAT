package splat.parser.elements;

import splat.lexer.Token;

public abstract class ASTElement {

	private Token tok;
	
	public ASTElement(Token tok) {
		this.tok = tok;
	}

	public Token getToken() { return tok; }
	
	public int getLine() {
		return tok.getLine();
	}

	public int getColumn() {
		return tok.getColumn();
	}
}
