package splat.parser.elements.declarations;

import splat.lexer.Token;
import splat.parser.elements.Declaration;
import splat.parser.elements.Type;

public class VariableDecl extends Declaration {

	private String label;
	private Type varType;

	public VariableDecl(Token tok, String label, Type varType) {
		super(tok);
		this.label = label;
		this.varType = varType;
	}

	public String getLabel() {
		return label;
	}

	public Type getVarType() {
		return varType;
	}

	public String toString() {
		return label + " : " + varType.toString() + ";";
	}

}
