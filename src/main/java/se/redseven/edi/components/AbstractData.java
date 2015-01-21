package se.redseven.edi.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/** Backing type (data container) for EDI types.<!-- -->
 * @see Element
 * @see Composite
 */
public abstract class AbstractData {

    protected ArrayList<String> values = null;

    /**
     * Get values.
     * @return values
     */
    public String[] getValues() {

        return values.toArray(new String[values.size()]);
    }

    /**
     * Set values.
     * @param values - B
     */
    public void setValues(String[] values) {

        this.values = new ArrayList<String>();
        Collections.addAll(this.values, values);
    }

    @Override
    public String toString() {

        return Arrays.toString(getValues());
    }
}
