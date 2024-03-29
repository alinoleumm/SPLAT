package splat.parser.elements.accesses;

import splat.executor.ExecutionException;
import splat.executor.Value;
import splat.lexer.Token;
import splat.parser.elements.LabelAccess;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDecl;
import splat.parser.elements.declarations.RectypeDecl;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

public class VariableOrParameter extends LabelAccess {

    private String label;

    public VariableOrParameter(Token tok, String label) {
        super(tok);
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        if(varAndParamMap.containsKey(label)) {
            Type type = varAndParamMap.get(label);
            return type;
        } else {
            throw new SemanticAnalysisException("variable not defined", this);
        }
    }

    public Value evaluate(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Value> varAndParamMap) throws ExecutionException {
        if(varAndParamMap.containsKey(label)) {
            Value val = varAndParamMap.get(label);
            return val;
        } else {
            throw new ExecutionException("variable not defined (execution)", this);
        }
    }

    public String toString() {
        return label;
    }

}
