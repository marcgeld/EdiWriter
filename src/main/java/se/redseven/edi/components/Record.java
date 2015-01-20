package se.redseven.edi.components;

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

    public Record() {

        this.name = this.getClass().getSimpleName();
    }

    /** @return the name */
    public String getName() {

        return name;
    }

    /** @param name the name to set */
    public void setName(String name) {

        this.name = name;
    }

    /** @param element value */
    public Record element(String value) {

        Element element = new Element(value);
        record.add(element);

        return this;
    }

    /** @param element repetition */
    public Record elementRep(int rep) {

        while (rep-- > 0) {

            record.add(new Element(""));
        }

        return this;
    }

    /** @param element value */
    public Record composite(String... values) {

        Composite composite = new Composite(values);
        record.add(composite);

        return this;
    }

    /** @param the settings for the message */
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

                recordValueBuffer.append(((Element) abstractData).getRecordValue(ediSettings));
            }
            else if (abstractData instanceof Composite) {

                recordValueBuffer.append(((Composite) abstractData).getRecordValue(ediSettings));
            }
        }

        // TODO: fix separator
        recordString =
            String.format("%s%s%c", this.getName(), recordValueBuffer.toString(), ediSettings.getEdiSegmentSeparator());

        LOG.debug(String.format("record: %s", String.valueOf(recordString)));

        return recordString;
    }

    private void createRecordFromAnnotations(ArrayList<AbstractData> record) {

        name = getClass().getSimpleName();

        this.record = getAnnotatedRecord(this);
    }
}
