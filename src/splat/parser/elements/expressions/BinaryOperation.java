package splat.parser.elements.expressions;

import splat.executor.ExecutionException;
import splat.executor.ReturnFromCall;
import splat.executor.Value;
import splat.executor.values.BooleanValue;
import splat.executor.values.IntegerValue;
import splat.executor.values.RecordValue;
import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDecl;
import splat.parser.elements.declarations.RectypeDecl;
import splat.parser.elements.types.ArrayType;
import splat.parser.elements.types.BooleanType;
import splat.parser.elements.types.IntegerType;
import splat.parser.elements.types.RecType;
import splat.parser.elements.vartypes.ArrayVarType;
import splat.parser.elements.vartypes.BooleanVarType;
import splat.parser.elements.vartypes.IntegerVarType;
import splat.parser.elements.vartypes.RecVarType;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

public class BinaryOperation extends Expression {

    private Expression exprLeft;
    private String binaryOp;
    private Expression exprRight;

    public BinaryOperation(Token tok, Expression exprLeft, String binaryOp, Expression exprRight) {
        super(tok);
        this.exprLeft = exprLeft;
        this.binaryOp = binaryOp;
        this.exprRight = exprRight;
    }

    public Expression getExprLeft() {
        return exprLeft;
    }

    public String getBinaryOp() {
        return binaryOp;
    }

    public Expression getExprRight() {
        return exprRight;
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
        Type leftType = exprLeft.analyzeAndGetType(funcMap,rectypeMap,varAndParamMap);
        Type rightType = exprRight.analyzeAndGetType(funcMap,rectypeMap,varAndParamMap);
        if(leftType instanceof ArrayType || leftType instanceof ArrayVarType) {
            leftType = getBaseType(leftType);
        }
        if(rightType instanceof ArrayType || rightType instanceof ArrayVarType) {
            rightType = getBaseType(rightType);
        }
        if(binaryOp.equals("+") || binaryOp.equals("-") || binaryOp.equals("*") || binaryOp.equals("/") || binaryOp.equals("%")) {
            if ((leftType instanceof IntegerType || leftType instanceof IntegerVarType) && (rightType instanceof IntegerType || rightType instanceof IntegerVarType)) {
                return new IntegerType(this.getToken());
            } else {
                throw new SemanticAnalysisException("Illegal type for +-*/% ops",this);
            }
        } else if(binaryOp.equals(">") || binaryOp.equals("<") || binaryOp.equals(">=") || binaryOp.equals("<=")) {
            if ((leftType instanceof IntegerType || leftType instanceof IntegerVarType) && (rightType instanceof IntegerType || rightType instanceof IntegerVarType)) {
                return new BooleanType(this.getToken());
            } else {
                throw new SemanticAnalysisException("Illegal type for ><= ops", this);
            }
        } else if(binaryOp.equals("and") || binaryOp.equals("or")) {
            if ((leftType instanceof BooleanType || leftType instanceof BooleanVarType) && (rightType instanceof BooleanType || rightType instanceof BooleanVarType)) {
                 return new BooleanType(this.getToken());
            } else {
                throw new SemanticAnalysisException("Illegal type for logical ops", this);
            }
        } else if(binaryOp.equals("=")) {
            if ((leftType instanceof IntegerType || leftType instanceof IntegerVarType) && (rightType instanceof IntegerType || rightType instanceof IntegerVarType) || (leftType instanceof BooleanType || leftType instanceof BooleanVarType) && (rightType instanceof BooleanType || rightType instanceof BooleanVarType)|| ((leftType instanceof RecType || leftType instanceof RecVarType) && exprRight.toString().equals("null"))) {
                return new BooleanType(this.getToken());
            } else {
                throw new SemanticAnalysisException("Illegal type for equals op", this);
            }
        } else {
            throw new SemanticAnalysisException("Illegal binary op", this);
        }
    }

    public Value evaluate(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Value> varAndParamMap) throws ExecutionException, ReturnFromCall {
        Value left = exprLeft.evaluate(funcMap,rectypeMap,varAndParamMap);
        Value right = exprRight.evaluate(funcMap,rectypeMap,varAndParamMap);
        if(binaryOp.equals("and") || binaryOp.equals("or")) {
            if((left instanceof BooleanValue) && (right instanceof BooleanValue)) {
                if(binaryOp.equals("and")) {
                    if (!((BooleanValue) left).isValue() || !((BooleanValue) right).isValue()) {
                        return new BooleanValue(false);
                    } else {
                        return new BooleanValue(true);
                    }
                } else {
                    if(!((BooleanValue) left).isValue() && !((BooleanValue) right).isValue()) {
                        return new BooleanValue(false);
                    } else {
                        return new BooleanValue(true);
                    }
                }
            } else {
                throw new ExecutionException("not boolean", this);
            }
        } if(binaryOp.equals("+") || binaryOp.equals("-") || binaryOp.equals("*") || binaryOp.equals("/") || binaryOp.equals("%")) {
            if((left instanceof IntegerValue) && (right instanceof IntegerValue)) {
                int res;
                if(binaryOp.equals("+")) {
                    res = ((IntegerValue) left).getValue() + ((IntegerValue) right).getValue();
                } else if(binaryOp.equals("-")) {
                    res = ((IntegerValue) left).getValue() - ((IntegerValue) right).getValue();
                } else if(binaryOp.equals("*")) {
                    res = ((IntegerValue) left).getValue() * ((IntegerValue) right).getValue();
                } else if(binaryOp.equals("/")) {
                    if(((IntegerValue) right).getValue()==0) {
                        throw new ExecutionException("cant divide by 0!!", this);
                    }
                    res = ((IntegerValue) left).getValue() / ((IntegerValue) right).getValue();
                } else {
                    res = ((IntegerValue) left).getValue() % ((IntegerValue) right).getValue();
                }
                return new IntegerValue(res);
            } else {
                throw new ExecutionException("not integer", this);
            }
        } if(binaryOp.equals(">") || binaryOp.equals("<") || binaryOp.equals(">=") || binaryOp.equals("<=")) {
            if((left instanceof IntegerValue) && (right instanceof IntegerValue)) {
                boolean res;
                if(binaryOp.equals(">")) {
                    res = ((IntegerValue) left).getValue() > ((IntegerValue) right).getValue();
                } else if(binaryOp.equals("<")) {
                    res = ((IntegerValue) left).getValue() < ((IntegerValue) right).getValue();
                } else if(binaryOp.equals(">=")) {
                    res = ((IntegerValue) left).getValue() >= ((IntegerValue) right).getValue();
                } else {
                    res = ((IntegerValue) left).getValue() <= ((IntegerValue) right).getValue();
                }
                return new BooleanValue(res);
            } else {
                throw new ExecutionException("not integer", this);
            }
        } else {
            boolean res;
            if((left instanceof IntegerValue) && (right instanceof IntegerValue)) {
                res =((IntegerValue) left).getValue() == ((IntegerValue) right).getValue();
            } else if((left instanceof BooleanValue) && (right instanceof BooleanValue)) {
                res = ((BooleanValue) left).isValue() == ((BooleanValue) right).isValue();
            } else if((left instanceof RecordValue) && exprRight.toString().equals("null")) {
                res = ((RecordValue) left).getValue() == null;
            } else {
                throw new ExecutionException("cant compare that", this);
            }
            return new BooleanValue(res);
        }
    }

    public String toString() {
        return "( " + exprLeft.toString() + " " + binaryOp + " " + exprRight.toString() + " ) ";
    }

}
