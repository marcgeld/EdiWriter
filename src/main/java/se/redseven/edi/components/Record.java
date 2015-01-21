package se.redseven.edi.components;

import static se.redseven.edi.utils.EdiUtils.truncateString;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.redseven.edi.EDIFACTSettings;
import se.redseven.edi.EdiWriter;
import se.redseven.edi.meta.annotation.AnnotationProcessor;
import se.redseven.edi.meta.annotation.EdiRecord;

public class Record extends AnnotationProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(EdiWriter.class);

    protected String name = null;
    protected ArrayList<AbstractData> record = new ArrayList<AbstractData>();

    /**
     * Constructor of a record container.
     */
    public Record() {

        this.name = this.getClass().getSimpleName();
    }

    /**
     * Get name of record
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
     * Create new element
     * @param value element name
     * @return the Record, same instance as invoked.
     */
    public Record element(String value) {

        Element element = new Element(value);
        record.add(element);

        return this;
    }

    /**
     * Short hand for creating multiple empty elements.
     * @param rep amount of time(s) a empty element should be added (repeated)
     * @return Record (this instance) after creating the repetition.
     */
    public Record elementRep(int rep) {

        while (rep-- > 0) {

            record.add(new Element(""));
        }

        return this;
    }

    /**
     * Create a composite from multiple (or one) Strings.
     * @param values String(s) with values that makes the composite.
     * @return Record (this instance) after creating the composite.
     */
    public Record composite(String... values) {

        Composite composite = new Composite(values);
        record.add(composite);

        return this;
    }

    /**
     * Get the formated record using EDIFACTSettings.
     * @param ediSettings settings to use.
     * @return String with a formated record
     */
    public String getRecord(EDIFACTSettings ediSettings) {

        final Class<? extends Record> clazz = this.getClass();
        final StringBuffer recordValueBuffer = new StringBuffer();
        String recordString = null;

        // Create Elements and Composites
        if (clazz.isAnnotationPresent(EdiRecord.class)) {

            // Exception UNA
            if (clazz.equals(UNA.class)) {

                UNA una = (UNA) this;

                return String.format("%s%s%s%s%s%s%s", una.getName(), una.CompositeSeparator, una.ElementSeparator,
                    una.DecimalNotation, una.ReleaseCharcter, una._reserved_for_future_use, una.SegmentSeparator);
            }

            // Not UNA, conform to general rules
            createRecordFromAnnotations(record);
        }

        // Read Elements and Composites
        for (AbstractData abstractData : record) {

            if (abstractData instanceof Element) {

                recordValueBuffer.append(((Element) abstractData).getFormatedValue(ediSettings));
            }
            else if (abstractData instanceof Composite) {

                recordValueBuffer.append(((Composite) abstractData).getFormatedValue(ediSettings));
            }
        }

        recordString =
            String.format("%s%s%c", this.getName(), recordValueBuffer.toString(), ediSettings.getRecordSeparator());

        LOG.debug(String.format("record: %s", String.valueOf(recordString)));

        if (ediSettings.isTruncating()) {

            return truncateString(recordString);
        }

        return recordString;
    }

    private void createRecordFromAnnotations(ArrayList<AbstractData> record) {

        name = getClass().getSimpleName();

        this.record = getAnnotatedRecord(this);
    }
}
