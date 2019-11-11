package splat.parser.elements.statements;

import splat.lexer.Token;
import splat.parser.elements.Statement;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDecl;
import splat.parser.elements.declarations.RectypeDecl;

import java.util.Map;

public class PrintLine extends Statement {

    public PrintLine(Token tok) {
        super(tok);
    }

    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Type> varAndParamMap) {

    }

    public String toString() {
        return "print_line";
    }
}
