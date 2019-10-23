package splat.lexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Lexer {

	private File progFile;
	private List<Token> tokens;
	private String validForLabel;
	private String validForSingleOp;

	public Lexer(File progFile) {
		this.progFile = progFile;
		this.tokens = new ArrayList<>();
		this.validForLabel = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_";
		this.validForSingleOp = ".,[]()<>=+-*/%:;";
	}

	public List<Token> tokenize() throws LexException {
		try {
			FileReader fr = new FileReader(progFile);
			BufferedReader br = new BufferedReader(fr);
			int i;
			String token = "";
			int line = 1;
			int column = 1;
			int startColumn = 1;
			boolean isLabel = false;
			boolean isDoubleOp = false;
			boolean isString = false;
			while ((i = br.read()) != -1) {
				if(isString) {
					if(i<32) {
						throw new LexException("Illegal character in a string",line,column);
					} else if((char)i=='"') {
						token = token + (char)i;
						column++;
						if(!token.isEmpty()) {
							tokens.add(new Token(token,line,startColumn));
							token = "";
							isString = false;
						}
						startColumn = column;
					} else {
						token = token + (char)i;
						column++;
					}
				} else if ((char) i == ' ' || (char) i == '\n' || (char) i == '\r' || (char) i == '\t') {
					if (!token.isEmpty()) {
						tokens.add(new Token(token,line,startColumn));
						token = "";
					}
					if((char) i == ' ') {
						column++;
					} else if ((char) i == '\n') {
						line++;
						column = 1;
					} else if ((char) i == '\t') {
						column = column + 4;
					}
					startColumn = column;
				} else if (validForLabel.indexOf((char) i) >= 0) {
					if (isDoubleOp) {
						if (!token.isEmpty()) {
							tokens.add(new Token(token,line,startColumn));
							token = "";
						}
						startColumn = column;
						isDoubleOp = false;
					}
					isLabel = true;
					token = token + (char) i;
					column++;
				} else if (validForSingleOp.indexOf((char) i) >= 0) {
					if (isLabel) {
						if (!token.isEmpty()) {
							tokens.add(new Token(token,line,startColumn));
							token = "";
						}
						startColumn = column;
						isLabel = false;
					} else if (isDoubleOp) {
						if ((char) i == '=') {
							token = token + (char) i;
							column++;
							if (!token.isEmpty()) {
								tokens.add(new Token(token,line,startColumn));
								token = "";
							}
							startColumn = column;
						} else {
							if (!token.isEmpty()) {
								tokens.add(new Token(token,line,startColumn));
								token = "";
							}
							startColumn = column;
							token = token + (char) i;
							column++;
							if (!token.isEmpty()) {
								tokens.add(new Token(token,line,startColumn));
								token = "";
							}
							startColumn = column;
						}
						isDoubleOp = false;
						continue;
					}
					token = token + (char) i;
					column++;
					if ((char) i == ':' || (char) i == '>' || (char) i == '<') {
						isDoubleOp = true;
					} else {
						if (!token.isEmpty()) {
							tokens.add(new Token(token,line,startColumn));
							token = "";
						}
						startColumn = column;
					}
				} else if ((char) i == '"') {
					if (isLabel) {
						if (!token.isEmpty()) {
							tokens.add(new Token(token,line,startColumn));
							token = "";
						}
						startColumn = column;
						isLabel = false;
					} else if (isDoubleOp) {
						if (!token.isEmpty()) {
							tokens.add(new Token(token,line,startColumn));
							token = "";
						}
						startColumn = column;
						isDoubleOp = false;
					}
					token = token + (char) i;
					column++;
					isString = true;
				} else {
					throw new LexException("Illegal character",line,column);
				}
			}
			if(!token.isEmpty()) {
				if(isString) {
					throw new LexException("Illegal character in a string",line,column);
				} else {
					tokens.add(new Token(token, line, startColumn));
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return tokens;
	}

}
