package splat.parser.elements;

import splat.lexer.Token;

public abstract class ASTElement {

	private Token tok;
//	private int line;
//	private int column;
	
	public ASTElement(Token tok) {
		this.tok = tok;
//		this.line = tok.getLine();
//		this.column = tok.getColumn();
	}

	public Token getToken() { return tok; }
	
	public int getLine() {
		return tok.getLine();
	}

	public int getColumn() {
		return tok.getColumn();
	}
}
