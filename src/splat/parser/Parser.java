package splat.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import splat.lexer.Token;
import splat.parser.elements.*;
import splat.parser.elements.accesses.ArrayAccess;
import splat.parser.elements.accesses.RecordFieldAccess;
import splat.parser.elements.accesses.VariableOrParameter;
import splat.parser.elements.declarations.FunctionDecl;
import splat.parser.elements.declarations.RectypeDecl;
import splat.parser.elements.declarations.VariableDecl;
import splat.parser.elements.expressions.*;
import splat.parser.elements.literals.BoolLiteral;
import splat.parser.elements.literals.IntLiteral;
import splat.parser.elements.literals.RectypeLiteral;
import splat.parser.elements.literals.StringLiteral;
import splat.parser.elements.other.FieldDeclaration;
import splat.parser.elements.other.Parameter;
import splat.parser.elements.statements.*;
import splat.parser.elements.types.*;
import splat.parser.elements.vartypes.*;

public class Parser {

	private List<Token> tokens;
	private List<String> reserved = Arrays.asList("program","begin","end","is","record","while","do","then","else","print","print_line","return","and","or","not","Integer","Boolean","String","true","false","null");
	private List<String> binaryOps = Arrays.asList("and","or",">","<","=",">=","<=","+","-","*","/","%");

	public Parser(List<Token> tokens) {
		this.tokens = tokens;
	}

	/**
	 * Compares the next token to an expected value, and throws
	 * an exception if they don't match.  This removes the front-most
	 * (next) token  
	 * 
	 * @param expected value of the next token
	 * @throws ParseException if the actual token doesn't match what 
	 * 			was expected
	 */
	private void checkNext(String expected) throws ParseException {

		Token tok = tokens.remove(0);
		
		if (!tok.getValue().equals(expected)) {
			throw new ParseException("Expected '"+ expected + "', got '" 
					+ tok.getValue()+ "'.", tok);
		}
	}
	
	/**
	 * Returns a boolean indicating whether or not the next token matches
	 * the expected String value.  This does not remove the token from the
	 * token list.
	 * 
	 * @param expected value of the next token
	 * @return true iff the token value matches the expected string
	 */
	private boolean peekNext(String expected) {
		return tokens.get(0).getValue().equals(expected);
	}
	
	/**
	 * Returns a boolean indicating whether or not the token directly after
	 * the front most token matches the expected String value.  This does 
	 * not remove any tokens from the token list.
	 * 
	 * @param expected value of the token directly after the next token
	 * @return true iff the value matches the expected string
	 */
	private boolean peekTwoAhead(String expected) {
		return tokens.get(1).getValue().equals(expected);
	}
	
	public ProgramAST parse() throws ParseException {
		
		try {
			// Needed for 'program' token position info
			Token startTok = tokens.get(0);
			checkNext("program");
			List<Declaration> decls = parseDecls();
			checkNext("begin");
			List<Statement> stmts = parseStmts();
			checkNext("end");
			checkNext(";");
			return new ProgramAST(decls, stmts, startTok);
			
		// This might happen if we do a tokens.get(), and nothing is there!
		} catch (IndexOutOfBoundsException ex) {
			throw new ParseException("Unexpectedly reached the end of file.", -1, -1);
		}
	}
	
	private List<Declaration> parseDecls() throws ParseException {
		
		List<Declaration> decls = new ArrayList<Declaration>();
		
		while (!peekNext("begin")) {
			Declaration decl = parseDecl();
			decls.add(decl);
		}
		
		return decls;
	}
	
	private Declaration parseDecl() throws ParseException {

		if (peekNext("record")) {
			return parseRectypeDecl();
		} else if (peekTwoAhead(":")) {
			return parseVarDecl();
		} else if (peekTwoAhead("(")) {
			return parseFuncDecl();
		} else {
			Token tok = tokens.get(0);
			throw new ParseException("Declaration expected", tok);
		}
	}

	private FunctionDecl parseFuncDecl() throws ParseException {
		Token labelToken = tokens.remove(0);
		String label = parseLabel(labelToken);
		checkNext("(");
		List<Parameter> params = parseParams();
		checkNext(")");
		checkNext(":");
		Type retType = parseType(tokens.remove(0));
		checkNext("is");
		List<VariableDecl> varDecls = parseVarDecls();
		checkNext("begin");
		List<Statement> stmts = parseStmts();
		checkNext("end");
		checkNext(";");
		FunctionDecl functionDecl = new FunctionDecl(labelToken,label,params,retType,varDecls,stmts);
		return functionDecl;
	}

	private List<VariableDecl> parseVarDecls() throws ParseException {
		List<VariableDecl> varDecls = new ArrayList<VariableDecl>();
		while(!peekNext("begin")) {
			VariableDecl varDecl = parseVarDecl();
			varDecls.add(varDecl);
		}
		return varDecls;
	}

	private VariableDecl parseVarDecl() throws ParseException {
		Token labelToken = tokens.remove(0);
		String label = parseLabel(labelToken);
		checkNext(":");
		VarType type = parseVarType(tokens.remove(0));
		VariableDecl varDecl = new VariableDecl(labelToken,label,type);
		checkNext(";");
		return varDecl;
	}

	private RectypeDecl parseRectypeDecl() throws ParseException {
		checkNext("record");
		Token labelToken = tokens.remove(0);
		String label = parseLabel(labelToken);
		checkNext("begin");
		List<FieldDeclaration> fieldDecls = parseFieldDecls();
		checkNext("end");
		checkNext(";");
		RectypeDecl rectypeDecl = new RectypeDecl(labelToken, label, fieldDecls);
		return rectypeDecl;
	}

	private List<FieldDeclaration> parseFieldDecls() throws ParseException {
		List<FieldDeclaration> fieldDecls = new ArrayList<FieldDeclaration>();
		while(!peekNext("end")) {
			FieldDeclaration fieldDecl = parseFieldDecl();
			fieldDecls.add(fieldDecl);
		}
		return fieldDecls;
	}

	private FieldDeclaration parseFieldDecl() throws ParseException {
		Token labelToken = tokens.remove(0);
		String label = parseLabel(labelToken);
		checkNext(":");
		Type type = parseType(tokens.remove(0));
		FieldDeclaration fieldDecl = new FieldDeclaration(label,type);
		checkNext(";");
		return fieldDecl;
	}
	
	private List<Statement> parseStmts() throws ParseException {
		List<Statement> stmts = new ArrayList<Statement>();
		while (!peekNext("end") && !peekNext("else")) {
			Statement stmt = parseStmt();
			stmts.add(stmt);
		}
		return stmts;
	}

	private Statement parseStmt() throws ParseException {
		if(peekNext("return") && peekTwoAhead(";")) {
			Token tok = tokens.remove(0);
			checkNext(";");
			Statement stmt = new Return(tok);
			return stmt;
		} else if(peekNext("return")) {
			Token tok = tokens.remove(0);
			Expression expr = parseExpr();
			checkNext(";");
			Statement stmt = new ReturnExpression(tok, expr);
			return stmt;
		} else if(peekNext("print_line")) {
			Token tok = tokens.remove(0);
			checkNext(";");
			Statement stmt = new PrintLine(tok);
			return stmt;
		} else if(peekNext("print")) {
			Token tok = tokens.remove(0);
			Expression expr = parseExpr();
			checkNext(";");
			Statement stmt = new PrintExpression(tok, expr);
			return stmt;
		} else if(peekNext("if")) {
			Token tok = tokens.remove(0);
			Expression expr = parseExpr();
			checkNext("then");
			List<Statement> stmtsTrue = parseStmts();
			if(peekNext("else")) {
				tokens.remove(0);
				List<Statement> stmtsFalse = parseStmts();
				checkNext("end");
				checkNext("if");
				checkNext(";");
				Statement stmt = new IfThenElse(tok,expr,stmtsTrue,stmtsFalse);
				return stmt;
			} else if(peekNext("end")) {
				tokens.remove(0);
				checkNext("if");
				checkNext(";");
				Statement stmt = new IfThen(tok,expr,stmtsTrue);
				return stmt;
			} else {
				throw new ParseException("Illegal statement declaration", tok);
			}
		} else if(peekNext("while")) {
			Token tok = tokens.remove(0);
			Expression expr = parseExpr();
			checkNext("do");
			List<Statement> stmts = parseStmts();
			checkNext("end");
			checkNext("while");
			checkNext(";");
			Statement stmt = new WhileLoop(tok, expr, stmts);
			return stmt;
 		} else if(peekTwoAhead("(")) {
			Token tok = tokens.remove(0);
			String label = parseLabel(tok);
			checkNext("(");
			List<Expression> args = parseArgs();
			checkNext(")");
			checkNext(";");
			Statement stmt = new VoidFunctionCall(tok, label, args);
			return stmt;
		} else if(peekTwoAhead(":=") || peekTwoAhead(".") || peekTwoAhead("[")) {
			Token tok = tokens.remove(0);
			String label = parseLabel(tok);
			LabelAccess varOrParam = new VariableOrParameter(tok,label);
			if(!peekNext(":=")) {
				LabelAccess labelAccess = parseLabelAccess(tok, varOrParam);
				checkNext(":=");
				Expression expr = parseExpr();
				checkNext(";");
				Statement stmt = new Assignment(tok, labelAccess, expr);
				return stmt;
			} else {
				checkNext(":=");
				Expression expr = parseExpr();
				checkNext(";");
				Statement stmt = new Assignment(tok, varOrParam, expr);
				return stmt;
			}
		} else {
			Token tok = tokens.remove(0);
			throw new ParseException("Illegal statement", tok);
		}
	}

	private LabelAccess parseLabelAccess(Token tok, LabelAccess l) throws ParseException {
		if(peekNext(".")) {
			checkNext(".");
			Token nextTok = tokens.remove(0);
			String field = parseLabel(nextTok);
			LabelAccess recField = new RecordFieldAccess(tok,l,field);
			if(peekNext(".") || peekNext("[")) {
				return parseLabelAccess(tok,recField);
			} else {
				return recField;
			}
		}  else if(peekNext("[")){
			checkNext("[");
			Expression expr = parseExpr();
			checkNext("]");
			LabelAccess arrayInd = new ArrayAccess(tok,l,expr);
			if(peekNext(".") || peekNext("[")) {
				return parseLabelAccess(tok,arrayInd);
			} else {
				return arrayInd;
			}
		} else {
			return l;
		}
	}


	private boolean isBinaryOp(String op) {
		if(binaryOps.contains(op)) {
			return true;
		} else {
			return false;
		}
	}

	//				if (peekTwoAhead("and") || peekTwoAhead("or") || peekTwoAhead(">") || peekTwoAhead("<")
//						|| peekTwoAhead("=") || peekTwoAhead(">=") || peekTwoAhead("<=") || peekTwoAhead("+")
//						|| peekTwoAhead("-") || peekTwoAhead("*") || peekTwoAhead("/") || peekTwoAhead("%")) {
//					Expression exprLeft = parseExpr();
//					String binaryOp = tokens.remove(0).toString();
//					Expression exprRight = parseExpr();
//					checkNext(")");
//					Expression expr = new BinaryOperation(tok, exprLeft, binaryOp, exprRight);
//					return expr;
//				} else {
//
//				}

	private Expression parseExpr() throws ParseException {
		if (peekNext("(")) {
			if (peekTwoAhead("not") || peekTwoAhead("-")) {
				Token tok = tokens.remove(0);
				String unaryOp = tokens.remove(0).toString();
				Expression exprIn = parseExpr();
				checkNext(")");
				Expression expr = new UnaryOperation(tok, unaryOp, exprIn);
				return expr;
			} else {
				Token tok = tokens.remove(0);
				Expression exprLeft = parseExpr();
				String binaryOp = tokens.remove(0).toString();
				if(isBinaryOp(binaryOp)) {
					Expression exprRight = parseExpr();
					checkNext(")");
					Expression expr = new BinaryOperation(tok, exprLeft, binaryOp, exprRight);
					return expr;
				} else {
					throw new ParseException("Illegal expression 1", tok);
				}
			}
		}  else if (peekTwoAhead("(")) {
			Token tok = tokens.remove(0);
			String label = parseLabel(tok);
			checkNext("(");
			List<Expression> args = parseArgs();
			checkNext(")");
			Expression expr = new NonVoidFunctionCall(tok, label, args);
			return expr;
		} else if (peekTwoAhead(".") || peekTwoAhead("[")) { // || isLabel(tokens.get(0))
			Token tok = tokens.remove(0);
			String label = parseLabel(tok);
			LabelAccess varOrParam = new VariableOrParameter(tok,label);
			LabelAccess labelAccess = parseLabelAccess(tok,varOrParam);
			Expression expr = new SingleLabelAccess(tok, labelAccess);
			return expr;
		} else {
			Token tok = tokens.remove(0);
			if(String.valueOf(tok).equals("true") || String.valueOf(tok).equals("false")) {
				Expression expr = new SingleLiteral(tok, new BoolLiteral(tok, Boolean.parseBoolean(tok.toString())));
				return expr;
			} else if(String.valueOf(tok).equals("null")) {
				Expression expr = new SingleLiteral(tok, new RectypeLiteral(tok));
				return expr;
			} else if(String.valueOf(tok).matches("\".*\"")) {
				Expression expr = new SingleLiteral(tok, new StringLiteral(tok, tok.toString()));
				return expr;
			} else if(String.valueOf(tok).matches("[1-9][0-9]*|0")) {
				Expression expr = new SingleLiteral(tok, new IntLiteral(tok, Integer.parseInt(tok.toString())));
				return expr;
			} else if(isLabel(tok)) {
				LabelAccess varOrParam = new VariableOrParameter(tok, tok.toString());
				Expression expr = new SingleLabelAccess(tok, varOrParam);
				return expr;
			} else {
				throw new ParseException("Illegal expression 2", tok);
			}
		}
	}

	private List<Expression> parseArgs() throws ParseException {
		List<Expression> args = new ArrayList<Expression>();
		if(!peekNext(")")) {
			while (true) {
				Expression expr = parseExpr();
				args.add(expr);
				if (peekNext(",")) {
					tokens.remove(0);
					continue;
				} else {
					break;
				}
			}
		}
		return args;
	}

	private String parseLabel(Token tok) throws ParseException {
		if(tok.toString().matches("[A-Za-z_][A-Za-z0-9_]*")) {
			if(!isReserved(tok.toString())) {
				return tok.toString();
			} else {
				throw new ParseException("Reserved keyword", tok);
			}
		} else {
			throw new ParseException("Illegal label", tok);
		}
	}

	private boolean isReserved(String label) {
		if(reserved.contains(label)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isLabel(Token tok) {
		if(tok.toString().matches("[A-Za-z_][A-Za-z0-9_]*")) {
			return true;
		} else {
			return false;
		}
	}

	private List<Parameter> parseParams() throws ParseException {
		List<Parameter> params = new ArrayList<Parameter>();
		if(!peekNext(")")) {
			while (true) {
				Parameter param = parseParam();
				params.add(param);
				if (peekNext(",")) {
					tokens.remove(0);
					continue;
				} else {
					break;
				}
			}
		}
 		return params;
	}

	private Parameter parseParam() throws ParseException {
		Token labelToken = tokens.remove(0);
		String label = parseLabel(labelToken);
		checkNext(":");
		Type type = parseType(tokens.remove(0));
		Parameter param = new Parameter(label,type);
		if(!peekNext(",") && !peekNext(")")) {
			throw new ParseException("Illegal character in parameters", labelToken);
		}
		return param;
	}

	private Type parseType(Token tok) throws ParseException {
		if(peekNext("[")) {
			if(tok.toString().equals("Integer")) {
				checkNext("[");
				checkNext("]");
				return new ArrayType(tok, new IntegerType(tok));
			} else if(tok.toString().equals("Boolean")) {
				checkNext("[");
				checkNext("]");
				return new ArrayType(tok, new BooleanType(tok));
			} else if(tok.toString().equals("String")) {
				checkNext("[");
				checkNext("]");
				return new ArrayType(tok, new StringType(tok));
			} else if(isLabel(tok)) {
				checkNext("[");
				checkNext("]");
				String token = parseLabel(tok);
				return new ArrayType(tok, new RecType(tok, token));
			} else {
				throw new ParseException("Illegal type", tok);
			}
		} else {
			if (tok.toString().equals("Integer")) {
				return new IntegerType(tok);
			} else if(tok.toString().equals("Boolean")) {
				return new BooleanType(tok);
			} else if(tok.toString().equals("String")) {
				return new StringType(tok);
			} else if(isLabel(tok)) {
				String token = parseLabel(tok);
				return new RecType(tok, token);
			} else if(tok.toString().equals("void")) {
				return new VoidType(tok);
			} else {
				throw new ParseException("Illegal type", tok);
			}
		}
	}

	private VarType parseVarType(Token tok) throws ParseException {
		if(peekNext("[")) {
			if(tok.toString().equals("Integer")) {
				tokens.remove(0);
				String intToken = tokens.remove(0).toString();
				if(intToken.matches("[1-9][0-9]*|0")) {
					IntLiteral intLit = new IntLiteral(tok, Integer.parseInt(intToken));
					checkNext("]");
					return new ArrayVarType(tok, new IntegerVarType(tok), intLit);
				} else {
					throw new ParseException("Illegal int literal", tok);
				}
			} else if(tok.toString().equals("Boolean")) {
				tokens.remove(0);
				String intToken = tokens.remove(0).toString();
				if(intToken.matches("[1-9][0-9]*|0")) {
					IntLiteral intLit = new IntLiteral(tok, Integer.parseInt(intToken));
					checkNext("]");
					return new ArrayVarType(tok, new BooleanVarType(tok), intLit);
				} else {
					throw new ParseException("Illegal int literal", tok);
				}
			} else if(tok.toString().equals("String")) {
				tokens.remove(0);
				String intToken = tokens.remove(0).toString();
				if(intToken.matches("[1-9][0-9]*|0")) {
					IntLiteral intLit = new IntLiteral(tok, Integer.parseInt(intToken));
					checkNext("]");
					return new ArrayVarType(tok, new StringVarType(tok), intLit);
				} else {
					throw new ParseException("Illegal int literal", tok);
				}
			} else if(isLabel(tok)) {
				tokens.remove(0);
				String token = parseLabel(tok);
				String intToken = tokens.remove(0).toString();
				if(intToken.matches("[1-9][0-9]*|0")) {
					IntLiteral intLit = new IntLiteral(tok, Integer.parseInt(intToken));
					checkNext("]");
					return new ArrayVarType(tok, new RecVarType(tok, token), intLit);
				} else {
					throw new ParseException("Illegal int literal", tok);
				}
			} else {
				throw new ParseException("Illegal type", tok);
			}
		} else {
			if (tok.toString().equals("Integer")) {
				return new IntegerVarType(tok);
			} else if(tok.toString().equals("Boolean")) {
				return new BooleanVarType(tok);
			} else if(tok.toString().equals("String")) {
				return new StringVarType(tok);
			} else if(isLabel(tok)) {
				String token = parseLabel(tok);
				return new RecVarType(tok, token);
			} else {
				throw new ParseException("Illegal type", tok);
			}
		}
	}

}
