package splat.parser.elements.statements;

import splat.executor.ExecutionException;
import splat.executor.ReturnFromCall;
import splat.executor.Value;
import splat.executor.values.BooleanValue;
import splat.executor.values.IntegerValue;
import splat.executor.values.StringValue;
import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.LabelAccess;
import splat.parser.elements.Statement;
import splat.parser.elements.Type;
import splat.parser.elements.accesses.VariableOrParameter;
import splat.parser.elements.declarations.FunctionDecl;
import splat.parser.elements.declarations.RectypeDecl;
import splat.parser.elements.types.ArrayType;
import splat.parser.elements.types.RecType;
import splat.parser.elements.vartypes.ArrayVarType;
import splat.parser.elements.vartypes.IntegerVarType;
import splat.parser.elements.vartypes.RecVarType;
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
        Type labelAccessType = labelAccess.analyzeAndGetType(funcMap,rectypeMap,varAndParamMap);
        if(labelAccessType instanceof ArrayType || labelAccessType instanceof ArrayVarType) {
            labelAccessType = getBaseType(labelAccessType);
        }
        Type exprType = expr.analyzeAndGetType(funcMap,rectypeMap,varAndParamMap);
        if(!labelAccessType.toString().equals(exprType.toString())) {
            System.out.println(labelAccessType.getClass().toString());
            System.out.println(exprType.getClass().toString());
            throw new SemanticAnalysisException("types of label access and expression dont match", this);
        }
    }

    public void execute(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Value> varAndParamMap) throws ReturnFromCall, ExecutionException {
        Value exprVal = expr.evaluate(funcMap,rectypeMap,varAndParamMap);
        if(labelAccess instanceof VariableOrParameter) {
            if(varAndParamMap.containsKey(((VariableOrParameter) labelAccess).getLabel())) {
                varAndParamMap.put(((VariableOrParameter) labelAccess).getLabel(),exprVal);
            } else {
                throw new ExecutionException("no such variable", this);
            }
        } else {
            System.out.println("idk");
        }
    }

    public String toString() {
        return labelAccess.toString() + " := " + expr.toString();
    }
}
