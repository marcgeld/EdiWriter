package se.redseven.ediwriter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Composites are one of the build blocks of the message.
 */
public class Composite extends AbstractData {

    private static final Logger LOG = LoggerFactory.getLogger(Composite.class);

    /**
     * Constructor
     * @param values String array with values.
     */
    public Composite(String... values) {

        super.values = new ArrayList<String>(10);

        for (String value : values) {

            super.values.add(value);
        }
    }

    /**
     * Constructor
     * @param values Java Collection List with values.
     */
    public Composite(List<String> values) {

        super.values = new ArrayList<String>(10);
        super.values.addAll(values);
    }

    /**
     * Get a value from the composite.
     * Return <tt>null</tt> if <tt>index</tt> is outside bounds.
     * @param index the selected index.
     * @return the String at index
     */
    public String getValue(int index) {

        if (0 < index || super.values.size() < index) {

            return super.values.get(index);
        }

        return null;
    }

    /**
     * The formated value of the composite.
     * @param ediSettings Settings that will be used for formating @see EDIFACTSettings.
     * @return The String representation of the Composite with separators.
     */
    public String getFormatedValue(EDIFACTSettings ediSettings) {

        StringBuffer sb = new StringBuffer();

        for (Iterator<String> iterator = values.iterator(); iterator.hasNext();) {

            String value = iterator.next();

            char sepChar = sb.length() == 0 ? ediSettings.getElementSeparator() : ediSettings.getCompositeSeparator();

            LOG.debug(String.format("Sepataror char: '%c', Value: '%s'", sepChar, String.valueOf(value)));

            sb.append(sepChar);
            sb.append(value);

        }

        String valueStr = sb.toString();

        return valueStr;
    }
}