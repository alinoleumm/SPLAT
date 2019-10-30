package splat.parser.elements.vartypes;

import splat.lexer.Token;
import splat.parser.elements.VarType;
import splat.parser.elements.literals.IntLiteral;

public class ArrayVarType extends VarType {

    private VarType varType;
    private IntLiteral intLiteral;

    public ArrayVarType(Token tok, VarType varType, IntLiteral intLiteral) {
        super(tok);
        this.varType = varType;
        this.intLiteral = intLiteral;
    }

    public VarType getVarType() {
        return varType;
    }

    public IntLiteral getIntLiteral() {
        return intLiteral;
    }

    public String toString() {
        return varType.toString() + "[" + intLiteral.toString() + "]";
    }
}
