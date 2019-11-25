package splat.parser.elements;

import splat.executor.ExecutionException;
import splat.executor.ReturnFromCall;
import splat.executor.Value;
import splat.lexer.Token;
import splat.parser.elements.declarations.FunctionDecl;
import splat.parser.elements.declarations.RectypeDecl;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.Map;

public abstract class Statement extends ASTElement {

	public Statement(Token tok) {
		super(tok);
	}

	/**
	 * This will be needed for Phase 3 - this abstract method will need to be
	 * implemented by every Statement subclass.  This method essentially does
	 * semantic analysis on the statement, and all sub-expressions that might
	 * make up the statement.   funcMap, rectypeMap, and varAndParamMap are 
	 * needed for performing semantic analysis and type retrieval for the 
	 * sub-expressions.
	 */
	 public abstract void analyze(Map<String, FunctionDecl> funcMap,
								  Map<String, RectypeDecl> rectypeMap,
								  Map<String, Type> varAndParamMap) throws SemanticAnalysisException;
	
	/**
	 * This will be needed for Phase 4 - this abstract method will need to be
	 * implemented by every Statement subclass.  This method is used to 
	 * execute each statement, which may result in output to the console, or
	 * updating the varAndParamMap.  All three maps may be needed for 
	 * evaluating any sub-expressions in the statement.
	 */
	 public abstract void execute(Map<String, FunctionDecl> funcMap,
								  Map<String, RectypeDecl> rectypeMap,
	                              Map<String, Value> varAndParamMap)
             throws ReturnFromCall, ExecutionException;
}
