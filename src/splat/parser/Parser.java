package splat.parser;

import java.util.ArrayList;
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

public class Parser {

	private List<Token> tokens;
	
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
	
	
	/*
	 *  <program> ::= program <decls> begin <stmts> end ;
	 */
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
			System.out.println(decl.toString());
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
		checkNext(";");
		checkNext("begin");
		List<Statement> stmts = parseStmts();
		checkNext("end");
		checkNext(";");
		FunctionDecl functionDecl = new FunctionDecl(labelToken,label,params,retType,varDecls,stmts);
		return functionDecl;
	}

	private List<VariableDecl> parseVarDecls() throws ParseException {
		List<VariableDecl> varDecls = new ArrayList<VariableDecl>();
		while(true) {
			VariableDecl varDecl = parseVarDecl();
			varDecls.add(varDecl);
			if(peekNext(",")) {
				tokens.remove(0);
				continue;
			} else {
				break;
			}
		}
		return varDecls;
	}

	private VariableDecl parseVarDecl() throws ParseException {
		Token labelToken = tokens.remove(0);
		String label = parseLabel(labelToken);
		checkNext(":");
		Type type = parseType(tokens.remove(0));
		VariableDecl varDecl = new VariableDecl(labelToken,label,type);
		if(!peekNext(",") && !peekNext(";")) {
			throw new ParseException("Illegal character in parameters", labelToken);
		}
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
		while (!peekNext("end")) {
			Statement stmt = parseStmt();
			System.out.println(stmt.toString());
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
			List<Expression> args = parseArgs();
			checkNext(")");
			checkNext(";");
			Statement stmt = new VoidFunctionCall(tok, label, args);
			return stmt;
		} else if(peekTwoAhead(":=") || peekTwoAhead(".") || peekTwoAhead("[")) {
			Token tok = tokens.remove(0);
			LabelAccess labelAccess = parseLabelAccess(tok);
			checkNext(":=");
			Expression expr = parseExpr();
			checkNext(";");
			Statement stmt = new Assignment(tok,labelAccess,expr);
			return stmt;
		} else {
			Token tok = tokens.remove(0);
			throw new ParseException("Illegal statement", tok);
		}
	}

	private LabelAccess parseLabelAccess(Token tok) throws ParseException {
		if(peekNext(".")) {
			LabelAccess labelAccessIn = parseLabelAccess(tok);
			checkNext(".");
			Token nextTok = tokens.remove(0);
			String label = parseLabel(nextTok);
			LabelAccess labelAccess = new RecordFieldAccess(tok, labelAccessIn, label);
			return labelAccess;
		}  else if(peekNext("[")){
			LabelAccess labelAccessIn = parseLabelAccess(tok);
			checkNext("[");
			Expression expr = parseExpr();
			checkNext("]");
			LabelAccess labelAccess = new ArrayAccess(tok, labelAccessIn, expr);
			return labelAccess;
		} else {
			String label = parseLabel(tok);
			LabelAccess labelAccess = new VariableOrParameter(tok, label);
			return labelAccess;
		}
	}

	private Expression parseExpr() throws ParseException {
		if (peekNext("(")) {
			Token tok = tokens.remove(0);
			if (peekTwoAhead("and") || peekTwoAhead("or") || peekTwoAhead(">") || peekTwoAhead("<")
					|| peekTwoAhead("=") || peekTwoAhead(">=") || peekTwoAhead("<=") || peekTwoAhead("+")
					|| peekTwoAhead("-") || peekTwoAhead("*") || peekTwoAhead("/") || peekTwoAhead("%")) {
				Expression exprLeft = parseExpr();
				String binaryOp = tokens.remove(0).toString();
				Expression exprRight = parseExpr();
				checkNext(")");
				Expression expr = new BinaryOperation(tok, exprLeft, binaryOp, exprRight);
				return expr;
			} else if (peekTwoAhead("not") || peekTwoAhead("-")) {
				String unaryOp = tokens.remove(0).toString();
				Expression exprIn = parseExpr();
				checkNext(")");
				Expression expr = new UnaryOperation(tok, unaryOp, exprIn);
				return expr;
			} else {
				throw new ParseException("Illegal expression", tok);
			}
		}  else if (peekTwoAhead("(")) {
			Token tok = tokens.remove(0);
			String label = parseLabel(tok);
			checkNext("(");
			List<Expression> args = parseArgs();
			checkNext(")");
			Expression expr = new NonVoidFunctionCall(tok, label, args);
			return expr;
		} else if (peekTwoAhead(".") || peekTwoAhead("[") || isLabel(tokens.get(0))) {
			Token tok = tokens.remove(0);
			LabelAccess labelAccess = parseLabelAccess(tok);
			Expression expr = new SingleLabelAccess(tok, labelAccess);
			return expr;
		} else {
			Token tok = tokens.remove(0);
			if(tok.toString().equals("true") || tok.toString().equals("false")) {
				Expression expr = new SingleLiteral(tok, new BoolLiteral(tok, Boolean.parseBoolean(tok.toString())));
				return expr;
			} else if(tok.toString().equals("null")) {
				Expression expr = new SingleLiteral(tok, new RectypeLiteral(tok));
				return expr;
			} else if(tok.toString().matches("\".*\"")) {
				Expression expr = new SingleLiteral(tok, new StringLiteral(tok, tok.toString()));
				return expr;
			} else if(tok.toString().matches("[1-9][0-9]*|0")) {
				Expression expr = new SingleLiteral(tok, new IntLiteral(tok, Integer.parseInt(tok.toString())));
				return expr;
			} else {
				throw new ParseException("Illegal expression", tok);
			}
		}
	}

	private List<Expression> parseArgs() throws ParseException {
		return null;
	}

	private String parseLabel(Token tok) throws ParseException {
		if(tok.toString().matches("[A-Za-z_][A-Za-z0-9_]*")) {
			return tok.toString();
		} else {
			throw new ParseException("Illegal label", tok);
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
		while(true) {
			Parameter param = parseParam();
			params.add(param);
			if(peekNext(",")) {
				tokens.remove(0);
				continue;
			} else {
				break;
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
		if(peekNext("[") && peekTwoAhead("]")) {
			if(tok.toString().equals("Integer")) {
				tokens.remove(0);
				tokens.remove(0);
				return new ArrayType(tok, new IntegerType(tok));
			} else if(tok.toString().equals("Boolean")) {
				tokens.remove(0);
				tokens.remove(0);
				return new ArrayType(tok, new BooleanType(tok));
			} else if(tok.toString().equals("String")) {
				tokens.remove(0);
				tokens.remove(0);
				return new ArrayType(tok, new StringType(tok));
			} else if(isLabel(tok)) {
				tokens.remove(0);
				tokens.remove(0);
				return new ArrayType(tok, new RecType(tok, tok.toString()));
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
				return new RecType(tok, tok.toString());
			} else if(tok.toString().equals("void")) {
				return new VoidType(tok);
			} else {
				throw new ParseException("Illegal type", tok);
			}
		}
	}

}
