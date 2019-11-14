package splat.parser.elements.statements;

import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.Statement;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDecl;
import splat.parser.elements.declarations.RectypeDecl;
import splat.parser.elements.other.Parameter;
import splat.parser.elements.types.VoidType;
import splat.semanticanalyzer.SemanticAnalysisException;

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

    public String toString() {
        return label + " ( " + args.toString() + " )";
    }
}
