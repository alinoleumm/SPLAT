package splat.executor.values;

import splat.executor.Value;

import java.util.Map;

public class RecordValue extends Value {

    private Map<String,Value> value;

    public RecordValue(Map<String,Value> value) {
        this.value = value;
    }

    public Map<String, Value> getValue() {
        return value;
    }

    public void setValue(Map<String, Value> value) {
        this.value = value;
    }

    @Override
    public String toString() {
        String ret = "{";
        for(String key : value.keySet()) {
            ret = ret + value.get(key).toString() + "; ";
        }
        ret = ret.substring(0,ret.length()-2);
        ret = ret + "}";
        return ret;
    }

}
