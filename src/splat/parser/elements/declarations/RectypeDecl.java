package splat.parser.elements.declarations;

import splat.lexer.Token;
import splat.parser.elements.Declaration;
import splat.parser.elements.other.FieldDeclaration;

import java.util.List;

public class RectypeDecl extends Declaration {

	private String label;
	private List<FieldDeclaration> fieldDecls;
	
	public RectypeDecl(Token tok, String label, List<FieldDeclaration> fieldDecls) {
		super(tok);
		this.label = label;
		this.fieldDecls = fieldDecls;
	}

	public String getLabel() {
		return label;
	}

	public List<FieldDeclaration> getFieldDecls() {
		return fieldDecls;
	}

	public String toString() {
		String str = "record " + label + " begin\n";
		for(int i=0; i<fieldDecls.size(); i++) {
			str = str + "\t" + fieldDecls.get(i).toString() + ";\n";
		}
		str = str + "end ;";
		return str;
	}

}