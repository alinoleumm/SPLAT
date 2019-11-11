package splat.parser.elements.declarations;

import splat.lexer.Token;
import splat.parser.elements.Declaration;
import splat.parser.elements.other.Parameter;
import splat.parser.elements.Statement;
import splat.parser.elements.Type;

import java.util.List;

public class FunctionDecl extends Declaration {

	private List<Parameter> params;
	private Type retType;
	private List<VariableDecl> varDecls;
	private List<Statement> stmts;

	public FunctionDecl(Token tok, String label, List<Parameter> params, Type retType, List<VariableDecl> varDecls, List<Statement> stmts) {
		super(tok, label);
		this.params = params;
		this.retType = retType;
		this.varDecls = varDecls;
		this.stmts = stmts;
	}

	public List<Parameter> getParams() {
		return params;
	}

	public Type getRetType() {
		return retType;
	}

	public List<VariableDecl> getVarDecls() {
		return varDecls;
	}

	public List<Statement> getStmts() {
		return stmts;
	}

	public String toString() {
		String str;
		str = this.getLabel() + " (";
		for(int k = 0; k < params.size(); k++) {
			str = str + params.get(k).toString();
			if (k != params.size() - 1) {
				str = str + ", ";
			}
		}
		str = str + ") : " + retType.toString() + " is\n";
		for(int i = 0; i < varDecls.size(); i++) {
			str = str + "\t" + varDecls.get(i).toString();
			if (i != varDecls.size() - 1) {
				str = str + ", ";
			}
		}
		str = str + "\nbegin\n";
		for(int j = 0; j<stmts.size(); j++) {
			str = str + "\t" + stmts.get(j).toString() + ";\n";
		}
		str = str + "end ;";
		return str;
	}

}
