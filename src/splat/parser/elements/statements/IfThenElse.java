package splat.parser.elements.statements;

import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.Statement;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDecl;
import splat.parser.elements.declarations.RectypeDecl;
import splat.parser.elements.types.BooleanType;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.List;
import java.util.Map;

public class IfThenElse extends Statement {

    private Expression expr;
    private List<Statement> stmtsTrue;
    private List<Statement> stmtsFalse;

    public IfThenElse(Token tok, Expression expr, List<Statement> stmtsTrue, List<Statement> stmtsFalse) {
        super(tok);
        this.expr = expr;
        this.stmtsTrue = stmtsTrue;
        this.stmtsFalse = stmtsFalse;
    }

    public Expression getExpr() {
        return expr;
    }

    public List<Statement> getStmtsTrue() {
        return stmtsTrue;
    }

    public List<Statement> getStmtsFalse() {
        return stmtsFalse;
    }

    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        Type exprType = expr.analyzeAndGetType(funcMap,rectypeMap,varAndParamMap);
        if(exprType instanceof BooleanType) {
            for(Statement stmt : stmtsTrue) {
                stmt.analyze(funcMap,rectypeMap,varAndParamMap);
            }
            for(Statement stmt : stmtsFalse) {
                stmt.analyze(funcMap,rectypeMap,varAndParamMap);
            }
        } else {
            throw new SemanticAnalysisException("wrong condition in ifthen", this);
        }
    }

    public String toString() {
        return "if " + expr.toString() + " then " + stmtsTrue.toString() + " else " + stmtsFalse.toString() + " end if";
    }
}
