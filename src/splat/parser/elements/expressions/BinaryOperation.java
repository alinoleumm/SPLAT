package splat.parser.elements.expressions;

import com.sun.org.apache.xpath.internal.operations.Bool;
import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDecl;
import splat.parser.elements.declarations.RectypeDecl;
import splat.parser.elements.types.BooleanType;
import splat.parser.elements.types.IntegerType;
import splat.parser.elements.types.VoidType;
import splat.parser.elements.types.RecType;
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

    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        Type leftType = exprLeft.analyzeAndGetType(funcMap,rectypeMap,varAndParamMap);
        Type rightType = exprRight.analyzeAndGetType(funcMap,rectypeMap,varAndParamMap);
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

    public String toString() {
        return "( " + exprLeft.toString() + " " + binaryOp + " " + exprRight.toString() + " ) ";
    }

}
