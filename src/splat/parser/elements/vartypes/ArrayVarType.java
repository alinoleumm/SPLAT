package splat.parser.elements.vartypes;

import splat.executor.Value;
import splat.executor.values.ArrayValue;
import splat.lexer.Token;
import splat.parser.elements.VarType;
import splat.parser.elements.literals.IntLiteral;

import java.util.ArrayList;
import java.util.List;

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

    public Value getInitialValue() {
        List<Value> values = new ArrayList();
        for(int i = 0; i<intLiteral.getIntLiteral(); i++) {
            values.add(varType.getInitialValue());
        }
        return new ArrayValue(values);
    }
}
