package splat.parser.elements.expressions;

import splat.executor.ExecutionException;
import splat.executor.ReturnFromCall;
import splat.executor.Value;
import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.LabelAccess;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDecl;
import splat.parser.elements.declarations.RectypeDecl;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

public class SingleLabelAccess extends Expression {

    private LabelAccess labelAccess;

    public SingleLabelAccess(Token tok, LabelAccess labelAccess) {
        super(tok);
        this.labelAccess = labelAccess;
    }

    public LabelAccess getLabelAccess() {
        return labelAccess;
    }

    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        return labelAccess.analyzeAndGetType(funcMap,rectypeMap,varAndParamMap);
    }

    public Value evaluate(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Value> varAndParamMap) throws ExecutionException, ReturnFromCall {
        return labelAccess.evaluate(funcMap,rectypeMap,varAndParamMap);
    }

    public String toString() {
        return labelAccess.toString();
    }

}
