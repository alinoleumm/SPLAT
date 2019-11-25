package splat.parser.elements.expressions;

import splat.executor.ExecutionException;
import splat.executor.ReturnFromCall;
import splat.executor.Value;
import splat.executor.values.*;
import splat.lexer.Token;
import splat.parser.elements.*;
import splat.parser.elements.declarations.FunctionDecl;
import splat.parser.elements.declarations.RectypeDecl;
import splat.parser.elements.declarations.VariableDecl;
import splat.parser.elements.other.FieldDeclaration;
import splat.parser.elements.other.Parameter;
import splat.parser.elements.types.ArrayType;
import splat.parser.elements.types.RecType;
import splat.parser.elements.vartypes.ArrayVarType;
import splat.parser.elements.vartypes.BooleanVarType;
import splat.parser.elements.vartypes.IntegerVarType;
import splat.parser.elements.vartypes.StringVarType;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NonVoidFunctionCall extends Expression {

    private String label;
    private List<Expression> args;

    public NonVoidFunctionCall(Token tok, String label, List<Expression> args) {
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

    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        if(funcMap.containsKey(label)) {
            List<Parameter> params = funcMap.get(label).getParams();
            if (params.size() != args.size()) {
                throw new SemanticAnalysisException("Number of parameters dont match", this);
            } else {
                for (int i = 0; i < args.size(); i++) {
                    Type argType = args.get(i).analyzeAndGetType(funcMap, rectypeMap, varAndParamMap);
                    Type paramType = params.get(i).getType();
                    if(argType instanceof ArrayType || argType instanceof ArrayVarType) {
                        argType = getBaseType(argType);
                    }
                    if(paramType instanceof ArrayType) {
                        paramType = getBaseType(paramType);
                    }
                    System.out.println(argType.getClass().toString());
                    System.out.println(paramType.getClass().toString());
                    if (!argType.toString().equals(paramType.toString())) {
                        throw new SemanticAnalysisException("Types of arg and param are different", this);
                    }
                }
                Type retType = funcMap.get(label).getRetType();
                if (retType.toString().equals("void")) {
                    throw new SemanticAnalysisException("Call to void function", this);
                } else {
                    return retType;
                }
            }
        } else if(rectypeMap.containsKey(label)) {
            List<FieldDeclaration> fieldDecls = rectypeMap.get(label).getFieldDecls();
            if(fieldDecls.size()!=args.size()) {
                throw new SemanticAnalysisException("Number of parameters dont match", this);
            } else {
                for (int i=0; i<args.size(); i++) {
                    Type argType = args.get(i).analyzeAndGetType(funcMap, rectypeMap, varAndParamMap);
                    Type fieldType = fieldDecls.get(i).getType();
                    if (argType.getClass()!=fieldType.getClass()) {
                        throw new SemanticAnalysisException("Types of arg and field are different", this);
                    }
                }
                return new RecType(this.getToken(),label);
            }
        } else {
            throw new SemanticAnalysisException("No such function or record", this);
        }
    }

    private List<Value> arrayInit(VarType t) {
        int arraySize = ((ArrayVarType) t).getIntLiteral().getIntLiteral();
        List<Value> arrayList = new ArrayList<Value>();
        if(((ArrayVarType) t).getVarType() instanceof ArrayVarType) {
            for(int i=0; i<arraySize; i++) {
                arrayList.add(new ArrayValue(arrayInit(((ArrayVarType) t).getVarType())));
            }
        } else if(((ArrayVarType) t).getVarType() instanceof IntegerVarType) {
            for(int i=0; i<arraySize; i++) {
                arrayList.add(new IntegerValue(0));
            }
        } else if(((ArrayVarType) t).getVarType() instanceof BooleanVarType) {
            for(int i=0; i<arraySize; i++) {
                arrayList.add(new BooleanValue(false));
            }
        } else if(((ArrayVarType) t).getVarType() instanceof StringVarType) {
            for(int i=0; i<arraySize; i++) {
                arrayList.add(new StringValue(""));
            }
        } else {
            for(int i=0; i<arraySize; i++) {
                arrayList.add(null);
            }
        }
        return arrayList;
    }

    public Value evaluate(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Value> varAndParamMap) throws ExecutionException, ReturnFromCall {
        if(funcMap.containsKey(label)) {
            List<VariableDecl> decls = funcMap.get(label).getVarDecls();
            Map<String, Value> funcVarAndParam = new HashMap<String, Value>();
            for(VariableDecl varDecl : decls) {
                if(varDecl.getVarType() instanceof IntegerVarType) {
                    funcVarAndParam.put(varDecl.getLabel(), new IntegerValue(0));
                } else if(varDecl.getVarType() instanceof BooleanVarType) {
                    funcVarAndParam.put(varDecl.getLabel(), new BooleanValue(false));
                } else if(varDecl.getVarType() instanceof StringVarType) {
                    funcVarAndParam.put(varDecl.getLabel(), new StringValue(""));
                } else if(varDecl.getVarType() instanceof ArrayVarType) {
                    funcVarAndParam.put(varDecl.getLabel(), new ArrayValue(arrayInit(varDecl.getVarType())));
                } else {
                    funcVarAndParam.put(varDecl.getLabel(),null);
                }
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
            } catch(ReturnFromCall ex) {
                return ex.getReturnVal();
            }
        } else if(rectypeMap.containsKey(label)) {
            Map<String,Value> fieldVals = new HashMap<String,Value>();
            List<FieldDeclaration> fieldDecls = rectypeMap.get(label).getFieldDecls();
            for(int i=0; i<args.size(); i++) {
                fieldVals.put(fieldDecls.get(i).getLabel(),args.get(i).evaluate(funcMap,rectypeMap,varAndParamMap));
            }
            return new RecordValue(fieldVals);
        } else {
            throw new ExecutionException("no such function nor record", this);
        }
        return null;
    }

    public String toString() {
        return label  + " ( " + args.toString() + " )";
    }

}
