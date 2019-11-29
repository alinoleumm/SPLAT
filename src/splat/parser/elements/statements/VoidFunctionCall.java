package splat.parser.elements.statements;

import splat.executor.ExecutionException;
import splat.executor.ReturnFromCall;
import splat.executor.Value;
import splat.executor.values.ArrayValue;
import splat.executor.values.BooleanValue;
import splat.executor.values.IntegerValue;
import splat.executor.values.StringValue;
import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.Statement;
import splat.parser.elements.Type;
import splat.parser.elements.VarType;
import splat.parser.elements.declarations.FunctionDecl;
import splat.parser.elements.declarations.RectypeDecl;
import splat.parser.elements.declarations.VariableDecl;
import splat.parser.elements.other.Parameter;
import splat.parser.elements.types.ArrayType;
import splat.parser.elements.types.VoidType;
import splat.parser.elements.vartypes.ArrayVarType;
import splat.parser.elements.vartypes.BooleanVarType;
import splat.parser.elements.vartypes.IntegerVarType;
import splat.parser.elements.vartypes.StringVarType;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoidFunctionCall extends Statement {

    private String label;
    private List<Expression> args;

    public VoidFunctionCall(Token tok, String label, List<Expression> args) {
        super(tok);
        this.label = label;
        this.args = args;
    }

    public String getLabel() {
        return label;
    }

    public List<Expression> getArgs() {
        return args;
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
        if(funcMap.containsKey(label)) {
            Type retType = funcMap.get(label).getRetType();
            if(retType instanceof VoidType) {
                List<Parameter> params = funcMap.get(label).getParams();
                if(params.size()!=args.size()) {
                    throw new SemanticAnalysisException("inconsistent number of args for params", this);
                } else {
                    for(int i=0; i<args.size(); i++) {
                        Type argType = args.get(i).analyzeAndGetType(funcMap, rectypeMap, varAndParamMap);
                        Type paramType = params.get(i).getType();
                        if(argType instanceof ArrayType || argType instanceof ArrayVarType) {
                            argType = getBaseType(argType);
                        }
                        if(paramType instanceof ArrayType) {
                            paramType = getBaseType(paramType);
                        }
                        if (!argType.toString().equals(paramType.toString())) {
                            throw new SemanticAnalysisException("Types of arg and param are different", this);
                        }
                    }
                }
            } else {
                throw new SemanticAnalysisException("call to nonvoid function", this);
            }
        } else {
            throw new SemanticAnalysisException("there is no such function", this);
        }
    }

    public void execute(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Value> varAndParamMap) throws ReturnFromCall, ExecutionException {
        List<VariableDecl> decls = funcMap.get(label).getVarDecls();
        Map<String, Value> funcVarAndParam = new HashMap<String, Value>();
        for(VariableDecl varDecl : decls) {
            funcVarAndParam.put(varDecl.getLabel(),varDecl.getVarType().getInitialValue());
        }
        List<Parameter> params = funcMap.get(label).getParams();
        for(int i=0; i<args.size(); i++) {
            funcVarAndParam.put(params.get(i).getLabel(),args.get(i).evaluate(funcMap,rectypeMap,varAndParamMap));
        }
        List<Statement> stmts = funcMap.get(label).getStmts();
        try {
            for (Statement stmt : stmts) {
                stmt.execute(funcMap, rectypeMap, funcVarAndParam);
            }
        } catch (ReturnFromCall ex){

        }
    }

    public String toString() {
        return label + " ( " + args.toString() + " )";
    }
}
