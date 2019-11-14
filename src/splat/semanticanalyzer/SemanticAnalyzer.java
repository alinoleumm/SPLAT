package splat.semanticanalyzer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import splat.parser.elements.*;
import splat.parser.elements.declarations.FunctionDecl;
import splat.parser.elements.declarations.RectypeDecl;
import splat.parser.elements.declarations.VariableDecl;
import splat.parser.elements.other.FieldDeclaration;
import splat.parser.elements.other.Parameter;
import splat.parser.elements.statements.IfThenElse;
import splat.parser.elements.statements.Return;
import splat.parser.elements.statements.ReturnExpression;
import splat.parser.elements.types.*;
import splat.parser.elements.vartypes.*;

public class SemanticAnalyzer {

	private ProgramAST progAST;

	private Map<String, FunctionDecl> funcMap;
	private Map<String, RectypeDecl> rectypeMap;
	private Map<String, Type> progVarMap;

	public SemanticAnalyzer(ProgramAST progAST) {
		this.progAST = progAST;
		this.funcMap = new HashMap<String, FunctionDecl>();
		this.rectypeMap = new HashMap<String, RectypeDecl>();
		this.progVarMap = new HashMap<String, Type>();
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
			if(stmt instanceof Return || stmt instanceof ReturnExpression) {
				throw new SemanticAnalysisException("cant have return statement in body!", progAST);
			}
			stmt.analyze(funcMap, rectypeMap, progVarMap);
		}

	}


	private Type varToType(ArrayVarType varType) {
		VarType t = varType.getVarType();
		Type tt;
		if(t instanceof IntegerVarType) {
			tt = new IntegerType(t.getToken());
		} else if(t instanceof BooleanVarType) {
			tt = new BooleanType(t.getToken());
		} else if(t instanceof StringVarType) {
			tt = new StringType(t.getToken());
		} else if(t instanceof ArrayVarType) {
			tt = new ArrayType(t.getToken(),varToType((ArrayVarType) t));
		} else {
			tt = new RecType(t.getToken(),((RecVarType) t).getLabel());
		}
		return tt;
	}


	private void checkTypeExistance(Type type) throws SemanticAnalysisException {

		if ((type instanceof ArrayType) || (type instanceof ArrayVarType)) {
			// Need to recursively check the base type
			ArrayType arrType;
			if(type instanceof ArrayVarType) {
				Type theType = varToType((ArrayVarType) type);
				arrType = new ArrayType(type.getToken(),theType);
			} else {
				arrType = (ArrayType) type;
			}
			checkTypeExistance(arrType.getType());
		} else if ((type instanceof BooleanType) || (type instanceof BooleanVarType) ||
				(type instanceof IntegerType) || (type instanceof IntegerVarType) ||
				(type instanceof StringType) || (type instanceof StringVarType) ||
				(type instanceof VoidType)) {
			// This is okay... no need to throw an exception here
		} else if ((type instanceof RecType) || (type instanceof RecVarType)){
			RecType recType;
			if (type instanceof RecVarType) {
				recType = new RecType(type.getToken(),((RecVarType) type).getLabel());
			} else {
				recType = (RecType) type;
			}
			if (!rectypeMap.containsKey(recType.getLabel())) { // there was a negation before
				throw new SemanticAnalysisException("Duplicate record field labels", recType);
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
		for (FieldDeclaration field : rectypeDecl.getFieldDecls()) {
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

		if (funcDecl.getRetType() instanceof VoidType) {
			for (Statement stmt : funcDecl.getStmts()) {
				if (stmt instanceof ReturnExpression) {
					throw new SemanticAnalysisException("void function cant return expression", progAST);
				} else if (stmt instanceof Return) {
					break;
				}
				stmt.analyze(funcMap, rectypeMap, varAndParamMap);
			}
		} else {
			Statement finalStmt = funcDecl.getStmts().get(funcDecl.getStmts().size()-1);
			int stmtId = 0;
			if(finalStmt instanceof ReturnExpression || finalStmt instanceof IfThenElse) {
				for(Statement stmt : funcDecl.getStmts()) {
					if(stmtId==funcDecl.getStmts().size()-1) {
						if(stmt instanceof IfThenElse) {
							stmt.analyze(funcMap,rectypeMap,varAndParamMap);
							Statement ifTrueLast = (Statement) ((IfThenElse) stmt).getStmtsTrue().get(((IfThenElse) stmt).getStmtsTrue().size()-1);
							Statement ifFalseLast = (Statement) ((IfThenElse) stmt).getStmtsFalse().get(((IfThenElse) stmt).getStmtsFalse().size()-1);
							if(!(ifTrueLast instanceof ReturnExpression) || !(ifFalseLast instanceof ReturnExpression)) {
								throw new SemanticAnalysisException("needs return for both if and else", progAST);
							} else {
								Type ifTrueLastRet = ((ReturnExpression) ifTrueLast).getExpr().analyzeAndGetType(funcMap,rectypeMap,varAndParamMap);
								Type ifFalseLastRet = ((ReturnExpression) ifFalseLast).getExpr().analyzeAndGetType(funcMap,rectypeMap,varAndParamMap);
								if(!ifTrueLastRet.toString().equals(funcDecl.getRetType().toString()) || !ifFalseLastRet.toString().equals(funcDecl.getRetType().toString())) {
									throw new SemanticAnalysisException("return type in ifthenelse is incorrect", progAST);
								}
							}
						} else {
							Type returnType = ((ReturnExpression) stmt).getExpr().analyzeAndGetType(funcMap,rectypeMap,varAndParamMap);
							if(!returnType.toString().equals(funcDecl.getRetType().toString())) {
								throw new SemanticAnalysisException("return type is incorrect", progAST);
							}
						}
					} else {
						if (stmt instanceof Return) {
							throw new SemanticAnalysisException("cant return in the middle of nonvoid function", progAST);
						}
						stmt.analyze(funcMap, rectypeMap, varAndParamMap);
						stmtId++;
					}
				}
			} else {
				throw new SemanticAnalysisException("nonvoid function must return expression", progAST);
			}
		}

		// Perform semantic analysis on the function body
//		for (Statement stmt : funcDecl.getStmts()) {
//			stmt.analyze(funcMap, rectypeMap, varAndParamMap);
//		}
	}


	private Map<String, Type> getVarAndParamMap(FunctionDecl funcDecl) {

		// This stores the variable and parameter, as well as return type
		// information for a specific function -- this is needed to do
		// semantic analysis for its body

		Map<String, Type> varAndParamMap = new HashMap<String, Type>();
		for (Parameter param : funcDecl.getParams()) {
			String paramLabel = param.getLabel();
			Type paramType = param.getType();
			varAndParamMap.put(paramLabel,paramType);
		}
		for (VariableDecl varDecl : funcDecl.getVarDecls()) {
			String varLabel = varDecl.getLabel();
			Type varType = varDecl.getVarType();
			varAndParamMap.put(varLabel,varType);
		}
		Type retType = funcDecl.getRetType();
		varAndParamMap.put("0return",retType);
		return varAndParamMap;

	}


	private void checkNoDuplicateFieldLabels(RectypeDecl rectypeDecl)
			throws SemanticAnalysisException {

        Set<String> labels = new HashSet<String>();
        for (FieldDeclaration field : rectypeDecl.getFieldDecls()) {
            String label = field.getLabel();
            if(labels.contains(label)) {
                throw new SemanticAnalysisException("Cannot have duplicate label '" + label + "' in program", field);
            } else {
                labels.add(label);
            }
        }

	}

	private void checkNoDuplicateFuncLabels(FunctionDecl funcDecl)
									throws SemanticAnalysisException {

        Set<String> labels = new HashSet<String>();
        for (Parameter param : funcDecl.getParams()) {
            String label = param.getLabel();
            if (labels.contains(label) || funcMap.containsKey(label) || rectypeMap.containsKey(label)) {
                throw new SemanticAnalysisException("Cannot have duplicate label '" + label + "' in program", param);
            } else {
                labels.add(label);
            }
        }
        for (VariableDecl varDecl : funcDecl.getVarDecls()) {
            String label = varDecl.getLabel();
            if (labels.contains(label) || funcMap.containsKey(label) || rectypeMap.containsKey(label)) {
                throw new SemanticAnalysisException("Cannot have duplicate label '" + label + "' in program", varDecl);
            } else {
                labels.add(label);
            }
        }

	}

	private void checkNoDuplicateProgLabels() throws SemanticAnalysisException {
		Set<String> labels = new HashSet<String>();
 		for (Declaration decl : progAST.getDecls()) {
 			String label = decl.getLabel();
			if (labels.contains(label)) {
				throw new SemanticAnalysisException("Cannot have duplicate label '" + label + "' in program", decl);
			} else {
				labels.add(label);
			}
		}

	}

	private void setMaps() {
		for (Declaration decl : progAST.getDecls()) {
			String label = decl.getLabel();
			if (decl instanceof FunctionDecl) {
				FunctionDecl funcDecl = (FunctionDecl)decl;
				funcMap.put(label, funcDecl);
			} else if (decl instanceof VariableDecl) {
				VariableDecl varDecl = (VariableDecl)decl;
				progVarMap.put(label, varDecl.getVarType());
			} else if (decl instanceof RectypeDecl) {
				RectypeDecl rectypeDecl = (RectypeDecl)decl;
				rectypeMap.put(label, rectypeDecl);
			}
		}
	}

}

