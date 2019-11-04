package splat.semanticanalyzer;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import splat.parser.elements.*;
import splat.parser.elements.declarations.FunctionDecl;
import splat.parser.elements.declarations.RectypeDecl;

public class SemanticAnalyzer {

	private ProgramAST progAST;
	
	private Map<String, FunctionDecl> funcMap;
	private Map<String, RectypeDecl> rectypeMap;
	private Map<String, Type> progVarMap;
	
	public SemanticAnalyzer(ProgramAST progAST) {
		this.progAST = progAST;
	}

	public void analyze() throws SemanticAnalysisException {
		
		// Checks to make sure we don't use the same labels more than once
		// for our program functions, variables, or record types 
		checkNoDuplicateProgLabels();
		
		// This sets the maps that will be needed later when we need to
		// typecheck variable references, function calls, and record type
		// uses in the program body
		setMaps();

		
		// Make sure any record types used in defining the variables are 
		// defined
		for (Type type : progVarMap.values()){
			checkTypeExistance(type);
		}
		
		// Make sure the field labels are not repeated within the same
		// record type declaration, and also make sure any record types
		// used in defining the fields are defined
		for (RectypeDecl rectypeDecl : rectypeMap.values()){
			analyzeRectypeDecl(rectypeDecl);
		}
		
		// Perform semantic analysis on the functions
		for (FunctionDecl funcDecl : funcMap.values()) {	
			analyzeFuncDecl(funcDecl);
		}
		
		// Perform semantic analysis on the program body
		for (Statement stmt : progAST.getStmts()) {
			stmt.analyze(funcMap, rectypeMap, progVarMap);
		}
		
	}
	


	private void checkTypeExistance(Type type) throws SemanticAnalysisException {
		
		// TODO:  You may need to modify the class names for representing types
		// here to get this to properly compile
		
		if (type instanceof ArrayType) {
			// Need to recursively check the base type
			ArrayType arrType = (ArrayType)type;
			checkTypeExistance(arrType.getBaseType());
		
		} else if (type instanceof BasicType) {
			// This is okay... no need to throw an exception here
			
		} else if (type instanceof RecordType) {
			RecordType recType = (RecordType)type;
			if (!rectypeMap.containsKey(recType.getLabel().toString())) {
				throw new SemanticAnalysisException("Duplicate record field labels", recType.getLabel());
			}
		}
		
	}
	
	private void analyzeRectypeDecl(RectypeDecl rectypeDecl) throws SemanticAnalysisException {
		
		// Checks to make sure we don't use the same field labels more than
		// once within this record type declaration
		checkNoDuplicateFieldLabels(rectypeDecl);
		
		// Checks to make sure that for any field types that make use of a record 
		// type (either directly or as the base type of an array), that the record
		// type is actually defined
		for (Field field : rectypeDecl.getFields()) {
			checkTypeExistance(field.getType());
		}
	}
	
	private void analyzeFuncDecl(FunctionDecl funcDecl) throws SemanticAnalysisException {
		
		// Checks to make sure we don't use the same labels more than once
		// among our function parameters, local variables, and function names
		checkNoDuplicateFuncLabels(funcDecl);
		
		// Get the types of the parameters and local variables
		Map<String, Type> varAndParamMap = getVarAndParamMap(funcDecl);
		
		// Make sure any used record types actually exist
		for (Type type : varAndParamMap.values()) {
			checkTypeExistance(type);
		}
		
		// Perform semantic analysis on the function body
		for (Statement stmt : funcDecl.getStmts()) {
			stmt.analyze(funcMap, rectypeMap, varAndParamMap);
		}
	}
	
	
	private Map<String, Type> getVarAndParamMap(FunctionDecl funcDecl) {
		
		// This stores the variable and parameter, as well as return type 
		// information for a specific function -- this is needed to do 
		// semantic analysis for its body
		
		// FIXME: The code here will look somewhat similar to setMaps()

		return null;
	}
	
	
	private void checkNoDuplicateFieldLabels(RectypeDecl rectypeDecl) 
			throws SemanticAnalysisException {

		// FIXME: Similar to checkNoDuplicateProgLabels()
	}
	
	private void checkNoDuplicateFuncLabels(FunctionDecl funcDecl) 
									throws SemanticAnalysisException {
		
		// FIXME: Similar to checkNoDuplicateProgLabels()
	}
	
	private void checkNoDuplicateProgLabels() throws SemanticAnalysisException {
		
		Set<String> labels = new HashSet<String>();
		
 		for (Declaration decl : progAST.getDecls()) {
 			String label = decl.getLabel().toString();
 			
			if (labels.contains(label)) {
				throw new SemanticAnalysisException("Cannot have duplicate label '"
						+ label + "' in program", decl);
			} else {
				labels.add(label);
			}
			
		}
	}
	
	private void setMaps() {
		
		for (Declaration decl : progAST.getDecls()) {

			String label = decl.getLabel().toString();
			
			if (decl instanceof FunctionDecl) {
				FunctionDecl funcDecl = (FunctionDecl)decl;
				funcMap.put(label, funcDecl);
				
			} else if (decl instanceof VariableDecl) {
				VariableDecl varDecl = (VariableDecl)decl;
				progVarMap.put(label, varDecl.getType());
			
			} else if (decl instanceof RectypeDecl) {
				RectypeDecl rectypeDecl = (RectypeDecl)decl;
				rectypeMap.put(label, rectypeDecl);
			}
		}
	}
}
