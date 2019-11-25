package splat.parser.elements.accesses;

import splat.executor.ExecutionException;
import splat.executor.ReturnFromCall;
import splat.executor.Value;
import splat.executor.values.ArrayValue;
import splat.executor.values.IntegerValue;
import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.LabelAccess;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDecl;
import splat.parser.elements.declarations.RectypeDecl;
import splat.parser.elements.types.IntegerType;
import splat.parser.elements.vartypes.IntegerVarType;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

public class ArrayAccess extends LabelAccess {

    private LabelAccess labelAccess;
    private Expression expr;

    public ArrayAccess(Token tok, LabelAccess labelAccess, Expression expr) {
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

    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        Type type = labelAccess.analyzeAndGetType(funcMap,rectypeMap,varAndParamMap);
        Type exprType = expr.analyzeAndGetType(funcMap,rectypeMap,varAndParamMap);
        if(exprType instanceof IntegerType || exprType instanceof IntegerVarType) {
            return type;
        } else {
            throw new SemanticAnalysisException("expression is not of integer type", this);
        }
    }

    public Value evaluate(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Value> varAndParamMap) throws ExecutionException, ReturnFromCall {
        Value val = labelAccess.evaluate(funcMap,rectypeMap,varAndParamMap);
        Value exprVal = expr.evaluate(funcMap,rectypeMap,varAndParamMap);
        if(exprVal instanceof IntegerValue) {
            if(val instanceof ArrayValue) {
                if(((IntegerValue) exprVal).getValue()>=0 && ((IntegerValue) exprVal).getValue()<((ArrayValue) val).getValue().size()) {
                    return ((ArrayValue) val).getValue().get(((IntegerValue) exprVal).getValue());
                } else {
                    throw new ExecutionException("index out of bound", this);
                }
            } else {
                throw new ExecutionException("not array!!", this);
            }
        } else {
            throw new ExecutionException("index is not integer", this);
        }
    }

    public String toString() {
        return labelAccess.toString() + " [ " + expr.toString() + " ]";
    }
}
