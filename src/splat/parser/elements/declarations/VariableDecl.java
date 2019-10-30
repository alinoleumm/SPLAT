package splat.parser.elements.declarations;

import splat.lexer.Token;
import splat.parser.elements.Declaration;
import splat.parser.elements.VarType;

public class VariableDecl extends Declaration {

	private String label;
	private VarType varType;

	public VariableDecl(Token tok, String label, VarType varType) {
		super(tok);
		this.label = label;
		this.varType = varType;
	}

	public String getLabel() {
		return label;
	}

	public VarType getVarType() {
		return varType;
	}

	public String toString() {
		return label + " : " + varType.toString() + ";";
	}

}
