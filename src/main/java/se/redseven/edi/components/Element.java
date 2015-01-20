package se.redseven.edi.components;

import java.util.ArrayList;

import se.redseven.edi.EDIFACTSettings;

public class Element extends AbstractData {

    /** Element constructor */
    public Element(String value) {

        super.values = new ArrayList<String>(1);
        super.values.add(value);
    }

    /** @return the value */
    public String getValue() {

        return super.values.get(0);
    }

    /** @param value the value to set */
    public void setValue(String value) {

        super.values.set(0, value);
    }

    public String getRecordValue(EDIFACTSettings ediSettings) {

        String value = String.format("%c%s", ediSettings.getEdiElementSeparator(), getValue());

        return value;
    }
}
