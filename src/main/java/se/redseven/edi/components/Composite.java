package se.redseven.edi.components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import se.redseven.edi.EDIFACTSettings;

/**
 * Composites are one of the build blocks of the message.
 */
public class Composite extends AbstractData {

    //private static final Logger LOG = LoggerFactory.getLogger(Composite.class);

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

        boolean firstSep = true;
        StringBuffer sb = new StringBuffer();
        Iterator<String> iter = values.iterator();

        do {

            if (firstSep) {

                sb.append(ediSettings.getElementSeparator());
                firstSep = false;
            }
            else {
                sb.append(ediSettings.getCompositeSeparator());

            }
            sb.append(iter.next());

        }
        while (iter.hasNext());

        String valueStr = sb.toString();

        return valueStr;
    }
}