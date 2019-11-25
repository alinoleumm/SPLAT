package splat.parser.elements.statements;

import splat.executor.ExecutionException;
import splat.executor.ReturnFromCall;
import splat.executor.Value;
import splat.executor.values.BooleanValue;
import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.Statement;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDecl;
import splat.parser.elements.declarations.RectypeDecl;
import splat.parser.elements.types.ArrayType;
import splat.parser.elements.types.BooleanType;
import splat.parser.elements.vartypes.ArrayVarType;
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

    private Type getBaseType(Type type)  {
        if(type instanceof ArrayType) {
            Type inType = ((ArrayType) type).getType();
            if(inType instanceof ArrayType) {
                return getBaseType(inType);
            } else {
                return inType;
            }
        } else {
            Type inType = ((ArrayVarType) type).getVarType();
            if(inType instanceof ArrayVarType) {
                return getBaseType(inType);
            } else {
                return inType;
            }
        }
    }

    public void analyze(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        Type exprType = expr.analyzeAndGetType(funcMap,rectypeMap,varAndParamMap);
        if(exprType instanceof ArrayType || exprType instanceof ArrayVarType) {
            exprType = getBaseType(exprType);
        }
        if(exprType instanceof BooleanType) {
            for(Statement stmt : stmts) {
                stmt.analyze(funcMap,rectypeMap,varAndParamMap);
            }
        } else {
            throw new SemanticAnalysisException("wrong condition in while loop", this);
        }
    }

    public void execute(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Value> varAndParamMap) throws ReturnFromCall, ExecutionException {
        while(((BooleanValue) expr.evaluate(funcMap,rectypeMap,varAndParamMap)).isValue()) {
            for(Statement stmt : stmts) {
                stmt.execute(funcMap,rectypeMap,varAndParamMap);
            }
        }
    }

    public String toString() {
        return "while " + expr.toString() + " do " + stmts.toString() + " end while";
    }

}
