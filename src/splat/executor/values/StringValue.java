package splat.executor.values;

import splat.executor.Value;

public class StringValue extends Value {

    private String value;

    public StringValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.substring(1,value.length()-1);
    }

}
