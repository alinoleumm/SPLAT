package splat.parser.elements.expressions;

import splat.executor.ExecutionException;
import splat.executor.ReturnFromCall;
import splat.executor.Value;
import splat.lexer.Token;
import splat.parser.elements.Expression;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDecl;
import splat.parser.elements.declarations.RectypeDecl;
import splat.parser.elements.other.Literal;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

public class SingleLiteral extends Expression {

    private Literal literal;

    public SingleLiteral(Token tok, Literal literal) {
        super(tok);
        this.literal = literal;
    }

    public Literal getLiteral() {
        return literal;
    }

    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        return literal.analyzeAndGetType(funcMap,rectypeMap,varAndParamMap);
    }

    public Value evaluate(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Value> varAndParamMap) throws ExecutionException, ReturnFromCall {
        return literal.evaluate(funcMap,rectypeMap,varAndParamMap);
    }

    public String toString() {
        return literal.toString();
    }
}
