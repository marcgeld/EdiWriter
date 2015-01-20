package se.redseven.edi.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public abstract class AbstractData {

    protected ArrayList<String> values = null;

    /** @return the value at index*/
    public String[] getValues() {

        return values.toArray(new String[values.size()]);
    }

    /** @param set value at index */
    public void setValues(String[] values) {

        this.values = new ArrayList<String>();
        Collections.addAll(this.values, values);
    }

    @Override
    public String toString() {

        return Arrays.toString(getValues());
    }
}
