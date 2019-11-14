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

public class WhileLoop extends Statement {

    private Expression expr;
    private List<Statement> stmts;

    public WhileLoop(Token tok, Expression expr, List<Statement> stmts) {
        super(tok);
        this.expr = expr;
        this.stmts = stmts;
    }

    public Expression getExpr() {
        return expr;
    }

    public List<Statement> getStmts() {
        return stmts;
    }

    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        Type exprType = expr.analyzeAndGetType(funcMap,rectypeMap,varAndParamMap);
        if(exprType instanceof BooleanType) {
            for(Statement stmt : stmts) {
                stmt.analyze(funcMap,rectypeMap,varAndParamMap);
            }
        } else {
            throw new SemanticAnalysisException("wrong condition in while loop", this);
        }
    }

    public String toString() {
        return "while " + expr.toString() + " do " + stmts.toString() + " end while";
    }

}
