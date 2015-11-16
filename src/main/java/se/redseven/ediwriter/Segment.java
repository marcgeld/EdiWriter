package se.redseven.ediwriter;

import java.util.ArrayList;
import java.util.List;

/**
 * Record.
 * @author ICC
 *
 */
public class Segment implements Constants {

    private String name = null;
    private boolean truncate = true;
    private ArrayList<AbstractData> segmentData = new ArrayList<AbstractData>();

    /**
     * Constructor of a record container.
     */
    public Segment() {

        this.name = this.getClass().getSimpleName();
    }

    /**
     * Constructor of a record container.
     * @param name Record name
     */
    public Segment(String name) {

        this.name = name;
    }

    /**
     * Get name of record.
     * @return name of record
     */
    public String getName() {

        return name;
    }

    /**
     * Set name of record. (Change name of record)
     * @param name the name of the record, UNH, UNB, etc.
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * Create new element.
     * @param value element name
     * @return the Record, same instance as invoked.
     */
    public Segment element(String value) {

        Element element = new Element(value);
        segmentData.add(element);

        return this;
    }

    /**
     * Short hand for creating multiple empty elements.
     * @param rep amount of time(s) a empty element should be added (repeated)
     * @return Record (this instance) after creating the repetition.
     */
    public Segment elementRep(int rep) {

        while (rep-- > 0) {

            segmentData.add(new Element(""));
        }

        return this;
    }

    /**
     * Create a composite from multiple (or one) Strings.
     * @param values String(s) with values that makes the composite.
     * @return Record (this instance) after creating the composite.
     */
    public Segment composite(String... values) {

        Composite composite = new Composite(values);
        segmentData.add(composite);

        return this;
    }

    /**
     * Create a composite from multiple (or one) Strings.
     * @param valueList List of string(s) with values that makes the composite.
     * @return Record (this instance) after creating the composite.
     */
    public Segment composite(List<String> valueList) {

        Composite composite = new Composite(valueList);
        segmentData.add(composite);

        return this;
    }

    public ArrayList<AbstractData> getRecordList() {

        return segmentData;
    }

    @Override
    public String toString() {

        StringBuffer sb = new StringBuffer();

        for (AbstractData abstractData : segmentData) {

            sb.append(abstractData.toString());
        }

        return String.valueOf(String.format("%s [%s]", String.valueOf(name), sb.toString()));
    }

    /**
     * Is EDIFACT truncating on/off.
     * @return the truncate
     */
    public boolean isTruncating() {

        return truncate;
    }

    /**
     * Set EDIFACT truncating on/off.
     * @param truncate the truncate to set
     */
    public void setTruncate(boolean truncate) {

        this.truncate = truncate;
    }
}
