package se.redseven.edi.components;

import java.util.ArrayList;

import se.redseven.edi.EDIFACTSettings;

/**
 * Elements are one of the build blocks of the message.
 */
public class Element extends AbstractData {

    /**
     * Element constructor.
     * @param value Element value.
     */
    public Element(String value) {

        super.values = new ArrayList<String>(1);
        super.values.add(value);
    }

    /**
     * Get element value.
     * @return element value
     */
    public String getValue() {

        return super.values.get(0);
    }

    /**
     * Set the element value.
     * @param value that value that are set.
     */
    public void setValue(String value) {

        super.values.set(0, value);
    }

    /**
     * The formated value of the record.
     * @param ediSettings Settings that will be used for formating @see EDIFACTSettings.
     * @return The String representation of the Element with separators.
     */
    public String getFormatedValue(EDIFACTSettings ediSettings) {

        String value = String.format("%c%s", ediSettings.getElementSeparator(), getValue());

        return value;
    }
}
