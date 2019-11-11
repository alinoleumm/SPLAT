package splat.parser.elements.declarations;

import splat.lexer.Token;
import splat.parser.elements.Declaration;
import splat.parser.elements.VarType;

public class VariableDecl extends Declaration {

	private VarType varType;

	public VariableDecl(Token tok, String label, VarType varType) {
		super(tok, label);
		this.varType = varType;
	}

	public VarType getVarType() {
		return varType;
	}

	public String toString() {
		return this.getLabel() + " : " + varType.toString() + ";";
	}

}
