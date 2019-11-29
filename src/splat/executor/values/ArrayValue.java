package splat.executor.values;

import splat.executor.Value;

import java.util.List;

public class ArrayValue extends Value {

    private List<Value> value;

    public ArrayValue(List<Value> value) {
        this.value = value;
    }

    public List<Value> getValue() {
        return value;
    }

    public void setValue(int index, Value val) {
        value.set(index,val);
    }

    @Override
    public String toString() {
        String ret = "[";
        for(Value val : value) {
            ret = ret + val.toString() + ", ";
        }
        ret = ret.substring(0,ret.length()-2);
        ret = ret + "]";
        return ret;
    }

}
