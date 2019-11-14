package splat.parser.elements.statements;

import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.LabelAccess;
import splat.parser.elements.Statement;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDecl;
import splat.parser.elements.declarations.RectypeDecl;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

public class Assignment extends Statement {

    private LabelAccess labelAccess;
    private Expression expr;

    public Assignment(Token tok, LabelAccess labelAccess, Expression expr) {
        super(tok);
        this.labelAccess = labelAccess;
        this.expr = expr;
    }

    public LabelAccess getLabelAccess() {
        return labelAccess;
    }

    public Expression getExpr() {
        return expr;
    }

    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        Type labelAccessType = labelAccess.analyzeAndGetType(funcMap,rectypeMap,varAndParamMap);
        Type exprType = expr.analyzeAndGetType(funcMap,rectypeMap,varAndParamMap);
        if(!labelAccessType.toString().equals(exprType.toString())) {
            throw new SemanticAnalysisException("types of label access and expression dont match", this);
        }
    }

    public String toString() {
        return labelAccess.toString() + " := " + expr.toString();
    }
}
