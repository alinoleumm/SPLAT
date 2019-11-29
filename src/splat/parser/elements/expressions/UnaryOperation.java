package splat.parser.elements.expressions;

import splat.executor.ExecutionException;
import splat.executor.ReturnFromCall;
import splat.executor.Value;
import splat.executor.values.BooleanValue;
import splat.executor.values.IntegerValue;
import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDecl;
import splat.parser.elements.declarations.RectypeDecl;
import splat.parser.elements.types.ArrayType;
import splat.parser.elements.types.BooleanType;
import splat.parser.elements.types.IntegerType;
import splat.parser.elements.vartypes.ArrayVarType;
import splat.parser.elements.vartypes.BooleanVarType;
import splat.parser.elements.vartypes.IntegerVarType;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

public class UnaryOperation extends Expression {

    private String unaryOp;
    private Expression expr;

    public UnaryOperation(Token tok, String unaryOp, Expression expr) {
        super(tok);
        this.unaryOp = unaryOp;
        this.expr = expr;
    }

    public String getUnaryOp() {
        return unaryOp;
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

    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        Type exprType = expr.analyzeAndGetType(funcMap,rectypeMap,varAndParamMap);
        if(exprType instanceof ArrayType || exprType instanceof ArrayVarType) {
            exprType = getBaseType(exprType);
        }
        if(unaryOp.equals("not")) {
            if(exprType instanceof BooleanType || exprType instanceof BooleanVarType) {
                return new BooleanType(this.getToken());
            } else {
                throw new SemanticAnalysisException("Illegal type for not", this);
            }
        } else if(unaryOp.equals("-")) {
            if(exprType instanceof IntegerType || exprType instanceof IntegerVarType) {
                return new IntegerType(this.getToken());
            } else {
                throw new SemanticAnalysisException("Illegal type for -", this);
            }
        } else {
            throw new SemanticAnalysisException("Illegal unary operator", this);
        }
    }

    public Value evaluate(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Value> varAndParamMap) throws ExecutionException, ReturnFromCall {
        Value val = expr.evaluate(funcMap,rectypeMap,varAndParamMap);
        if(unaryOp.equals("not")) {
            if(val instanceof BooleanValue) {
                if(((BooleanValue) val).isValue()) {
                    return new BooleanValue(false);
                } else {
                    return new BooleanValue(true);
                }
            } else {
                throw new ExecutionException("value is not boolean", this);
            }
        } else {
            if(val instanceof IntegerValue) {
                return new IntegerValue(-((IntegerValue) val).getValue());
            } else {
                throw new ExecutionException("value is not integer", this);
            }
        }
    }

    public String toString() {
        return "( " + unaryOp + " " + expr.toString() + " )";
    }

}