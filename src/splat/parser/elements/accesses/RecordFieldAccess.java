package splat.parser.elements.accesses;

import splat.executor.ExecutionException;
import splat.executor.ReturnFromCall;
import splat.executor.Value;
import splat.executor.values.RecordValue;
import splat.lexer.Token;
import splat.parser.elements.LabelAccess;
import splat.parser.elements.Type;
import splat.parser.elements.declarations.FunctionDecl;
import splat.parser.elements.declarations.RectypeDecl;
import splat.parser.elements.other.FieldDeclaration;
import splat.parser.elements.types.ArrayType;
import splat.parser.elements.types.RecType;
import splat.parser.elements.vartypes.ArrayVarType;
import splat.parser.elements.vartypes.RecVarType;
import splat.semanticanalyzer.SemanticAnalysisException;

import java.util.List;
import java.util.Map;

public class RecordFieldAccess extends LabelAccess {

    private LabelAccess labelAccess;
    private String field;

    public RecordFieldAccess(Token tok, LabelAccess labelAccess, String field) {
        super(tok);
        this.labelAccess = labelAccess;
        this.field = field;
    }

    public LabelAccess getLabelAccess() {
        return labelAccess;
    }

    public String getField() {
        return field;
    }

    private Type getBaseType(Type type) throws SemanticAnalysisException {
        if(type instanceof ArrayType) {
            Type inType = ((ArrayType) type).getType();
            if(inType instanceof RecType) {
                return inType;
            } else if(inType instanceof ArrayType) {
                return getBaseType(inType);
            } else {
                throw new SemanticAnalysisException("shouldnt happen", this);
            }
        } else {
            Type inType = ((ArrayVarType) type).getVarType();
            if(inType instanceof RecVarType) {
                return inType;
            } else if(inType instanceof ArrayVarType) {
                return getBaseType(inType);
            } else {
                throw new SemanticAnalysisException("shouldnt happen", this);
            }
        }
    }

    public Type analyzeAndGetType(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Type> varAndParamMap) throws SemanticAnalysisException {
        Type type = labelAccess.analyzeAndGetType(funcMap,rectypeMap,varAndParamMap);
        if(type instanceof ArrayType || type instanceof ArrayVarType) {
            type = getBaseType(type);
        }
        if(type instanceof RecType || type instanceof RecVarType) {
            if(type instanceof RecVarType) {
                type = new RecType(type.getToken(), ((RecVarType) type).getLabel());
            }
            if(rectypeMap.containsKey(((RecType) type).getLabel())) {
                List<FieldDeclaration> fields = rectypeMap.get(((RecType) type).getLabel()).getFieldDecls();
                for(FieldDeclaration f : fields) {
                    if(f.getLabel().equals(field)) {
                        return f.getType();
                    }
                }
                throw new SemanticAnalysisException("no such field", this);
            } else {
                throw new SemanticAnalysisException("no such record", this);
            }
        } else {
            throw new SemanticAnalysisException("not a rectype", this);
        }
    }

    public Value evaluate(Map<String, FunctionDecl> funcMap, Map<String, RectypeDecl> rectypeMap, Map<String, Value> varAndParamMap) throws ExecutionException, ReturnFromCall {
        Value val = labelAccess.evaluate(funcMap,rectypeMap,varAndParamMap);
        if(val!=null) {
            if(val instanceof RecordValue) {
                if(((RecordValue) val).getValue().containsKey(field)) {
                    return ((RecordValue) val).getValue().get(field);
                } else {
                    throw new ExecutionException("no such field", this);
                }
            } else {
                throw new ExecutionException("not a record", this);
            }
        } else {
            throw new ExecutionException("cant access null struct!", this);
        }
    }

    public String toString() {
        return labelAccess.toString() + " . " + field;
    }

}
