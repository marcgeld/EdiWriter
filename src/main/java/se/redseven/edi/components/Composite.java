package se.redseven.edi.components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import se.redseven.edi.EDIFACTSettings;

public class Composite extends AbstractData {

    //private static final Logger LOG = LoggerFactory.getLogger(Composite.class);

    public Composite(String... values) {

        super.values = new ArrayList<String>(10);

        for (String value : values) {

            super.values.add(value);
        }
    }

    public Composite(List<String> values) {

        super.values = new ArrayList<String>(10);
        super.values.addAll(values);
    }

    /** @return the value at index*/
    public String getValue(int index) {

        return super.values.get(index);
    }

    /** Flatten collection to a separated String */
    public String getRecordValue(EDIFACTSettings ediSettings) {

        boolean firstSep = true;
        StringBuffer sb = new StringBuffer();
        Iterator<String> iter = values.iterator();

        do {

            if (firstSep) {

                sb.append(ediSettings.getEdiElementSeparator());
                firstSep = false;
            }
            else {
                sb.append(ediSettings.getEdiCompositeSeparator());

            }
            sb.append(iter.next());

        }
        while (iter.hasNext());

        String valueStr = sb.toString();

        return valueStr;
    }
}