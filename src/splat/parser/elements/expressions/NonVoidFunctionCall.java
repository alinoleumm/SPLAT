package splat.parser.elements.expressions;

import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDecl;
import splat.parser.elements.declarations.RectypeDecl;
import splat.parser.elements.other.FieldDeclaration;
import splat.parser.elements.other.Parameter;
import splat.parser.elements.types.RecType;
import splat.semanticanalyzer.SemanticAnalysisException;

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

    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        if(funcMap.containsKey(label)) {
            List<Parameter> params = funcMap.get(label).getParams();
            if (params.size() != args.size()) {
                throw new SemanticAnalysisException("Number of parameters dont match", this);
            } else {
                for (int i = 0; i < args.size(); i++) {
                    Type argType = args.get(i).analyzeAndGetType(funcMap, rectypeMap, varAndParamMap);
                    Type paramType = params.get(i).getType();
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

    public String toString() {
        return label  + " ( " + args.toString() + " )";
    }

}
