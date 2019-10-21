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

	public Lexer(File progFile) {
		this.progFile = progFile;
		this.tokens = new ArrayList<>();
	}

	public List<Token> tokenize() throws LexException {
		try {
			FileReader fr = new FileReader(progFile);
			BufferedReader br = new BufferedReader(fr);
			int i;
			String token = "";
//			int line = 0;
//			int column = 0;
//			int startColumn = 0;
			while ((i = br.read()) != -1) {
				System.out.println((char) Character.toLowerCase(i));
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return tokens;
	}

}
