package splat.executor;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import splat.executor.values.*;
import splat.parser.elements.Declaration;
import splat.parser.elements.ProgramAST;
import splat.parser.elements.Statement;
import splat.parser.elements.VarType;
import splat.parser.elements.declarations.FunctionDecl;
import splat.parser.elements.declarations.RectypeDecl;
import splat.parser.elements.declarations.VariableDecl;
import splat.parser.elements.vartypes.*;

public class Executor {

	private ProgramAST progAST;
	
	private Map<String, FunctionDecl> funcMap;
	private Map<String, RectypeDecl> rectypeMap;
	private Map<String, Value> progVarMap;
	
	public Executor(ProgramAST progAST) {
		this.progAST = progAST;
		this.funcMap = new HashMap<String,FunctionDecl>();
		this.rectypeMap = new HashMap<String,RectypeDecl>();
		this.progVarMap = new HashMap<String,Value>();
	}

	public void runProgram() throws ExecutionException {

		// This sets the maps that will be needed for executing function 
		// calls, creating and updating records, and storing the values 
		// of the program variables
		setMaps();

		try {
			// Go through and execute each of the statements
			for (Statement stmt : progAST.getStmts()) {
				stmt.execute(funcMap, rectypeMap, progVarMap);
			}
			
		// We should never have to catch this exception here, since the
		// main program body cannot have returns
		} catch (ReturnFromCall ex) {
			System.out.println("Internal error!!! The main program body "
					+ "cannot have a return statement -- this should have "
					+ "been caught during semantic analysis!");

			throw new ExecutionException("Internal error -- fix your "
					+ "semantic analyzer!", -1, -1);
		}
	}
//
//	private List<Value> arrayInit(VarType t) {
//		int arraySize = ((ArrayVarType) t).getIntLiteral().getIntLiteral();
//		List<Value> arrayList = new ArrayList<Value>();
//		if(((ArrayVarType) t).getVarType() instanceof ArrayVarType) {
//			for(int i=0; i<arraySize; i++) {
//				arrayList.add(new ArrayValue(arrayInit(((ArrayVarType) t).getVarType())));
//			}
//		} else if(((ArrayVarType) t).getVarType() instanceof IntegerVarType) {
//			for(int i=0; i<arraySize; i++) {
//				arrayList.add(new IntegerValue(0));
//			}
//		} else if(((ArrayVarType) t).getVarType() instanceof BooleanVarType) {
//			for(int i=0; i<arraySize; i++) {
//				arrayList.add(new BooleanValue(false));
//			}
//		} else if(((ArrayVarType) t).getVarType() instanceof StringVarType) {
//			for(int i=0; i<arraySize; i++) {
//				arrayList.add(new StringValue(""));
//			}
//		} else {
//			for(int i=0; i<arraySize; i++) {
//				arrayList.add(null);
//			}
//		}
//		return arrayList;
//	}
	
	private void setMaps() {
		for (Declaration decl : progAST.getDecls()) {
			String label = decl.getLabel();
			if (decl instanceof FunctionDecl) {
				FunctionDecl funcDecl = (FunctionDecl)decl;
				funcMap.put(label, funcDecl);
			} else if (decl instanceof VariableDecl) {
				VariableDecl varDecl = (VariableDecl)decl;
				progVarMap.put(label,varDecl.getVarType().getInitialValue());
				/*
				if(varDecl.getVarType() instanceof IntegerVarType) {
					progVarMap.put(label, new IntegerValue(0));
				} else if(varDecl.getVarType() instanceof BooleanVarType) {
					progVarMap.put(label, new BooleanValue(false));
				} else if(varDecl.getVarType() instanceof StringVarType) {
					progVarMap.put(label, new StringValue(""));
				} else if(varDecl.getVarType() instanceof ArrayVarType) {
					progVarMap.put(label, new ArrayValue(arrayInit(varDecl.getVarType())));
				} else {
					progVarMap.put(label,null);
				}
				 */
			} else if (decl instanceof RectypeDecl) {
				RectypeDecl rectypeDecl = (RectypeDecl)decl;
				rectypeMap.put(label, rectypeDecl);
			}
		}
	}

}
