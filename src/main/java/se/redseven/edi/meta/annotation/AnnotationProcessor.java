package se.redseven.edi.meta.annotation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.redseven.edi.Constants;
import se.redseven.edi.components.AbstractData;
import se.redseven.edi.components.Composite;
import se.redseven.edi.components.Element;
import se.redseven.edi.error.ParserException;

/**
 * Class that process annotation of compiled classes.
 * If classes are added, don't forget to add '@Retention(RetentionPolicy.RUNTIME)' to the class.
 *
 * <p>
 *  It is very important to add the '@Retention(RetentionPolicy.RUNTIME)' annotation.
 *  Without that annotation, the compiler will discard any custom annotation in the class file.
 */
public class AnnotationProcessor implements Constants {

    private static final Logger LOG = LoggerFactory.getLogger(AnnotationProcessor.class);
    private List<String> TEMPLATE_TEXT_LIST = Arrays.asList(Constants.TEMPLATE_TEXT);

    /**
     * Get an List with all data associated with this annotated object.
     * @param obj object that has the annotated record
     * @return All data associated with this record.
     */
    public ArrayList<AbstractData> getAnnotatedRecord(Object obj) {

        ArrayList<AbstractData> recordList = new ArrayList<AbstractData>();

        String recName = obj.getClass().getSimpleName();
        Field[] fields = obj.getClass().getFields();

        for (Field field : fields) {

            if (field.isAnnotationPresent(EdiComposite.class)) {

                // compositeGroup can be n:th composite(s)!
                List<Composite> compositeGroup = getCompositeList(field, obj);

                if (checkCompositeRepetitions(field, compositeGroup)) {

                    if (0 == compositeGroup.size()) {

                        // Add empty element
                        LOG.debug(String.format("Empty composite: '%s' record '%s'.", field.getName(), recName));
                        recordList.add(new Element(""));
                    }
                    else {
                        // Loop over compositeGroup
                        for (Composite composite : compositeGroup) {

                            Class<? extends Composite> compositeClass = composite.getClass();
                            String compositeName = compositeClass.getSimpleName();
                            Field[] compositeFields = compositeClass.getFields();

                            LOG.debug(String.format("Composite: '%s' record '%s'.", compositeName, recName));

                            //composite.setValues()
                            ArrayList<String> elementValueList = new ArrayList<String>();
                            for (Field compositeField : compositeFields) {

                                if (compositeField.isAnnotationPresent(EdiElement.class)) {

                                    // Get Value
                                    String elementValue = getElementValue(compositeField, composite);

                                    if (null != elementValue) {

                                        elementValueList.add(elementValue);
                                    }
                                }
                            }

                            if (elementValueList.size() > 0) {

                                recordList.add(new Composite(elementValueList));
                            }
                        }
                    }
                }
            }
            else if (field.isAnnotationPresent(EdiElement.class)) {

                // Get Value
                String elementValue = getElementValue(field, obj);

                if (null != elementValue) {

                    recordList.add(new Element(elementValue));
                }
            }
        }

        return recordList;
    }

    /**
     * Get values from an element
     *
     * @param field the active field.
     * @param obj object with the element (field).
     * @return Value of that element of that object.
     */
    public String getElementValue(Field field, Object obj) {

        String cName = obj.getClass().getSimpleName();
        String fName = field.getName();
        String value = null;
        EdiElement ediElement = field.getAnnotation(EdiElement.class);
        boolean elementMandatory = ediElement.mandatory();
        int elementLength = ediElement.length();
        String defValue = ediElement.value();

        Object elemObj = getObject(field, obj);

        if (elemObj == null) {

            value = "";
        }
        else {

            value = String.valueOf(elemObj);
        }

        LOG.debug(String.format("Element '%s' value '%s' default value '%s' max-lenght=%d class '%s'.", fName,
            String.valueOf(value), String.valueOf(defValue), elementLength, cName));

        if (defValue != null && !defValue.equals("") && value.equals("")) {

            value = String.valueOf(defValue);
        }

        // Check length
        if (value.length() > elementLength) {

            // Exclude from check...
            if (!TEMPLATE_TEXT_LIST.contains(value)) {

                throw new ParserException(String.format(
                    "Error: Element '%s' to long, value='%s' (lenght=%d), max=%d class '%s'.", fName, value,
                    value.length(), elementLength, cName));
            }
        }

        // Check if Mandatory
        if (elementMandatory && value.trim().equals("")) {

            throw new ParserException(String.format(
                "Error: Element '%s' has no value '%s' (lenght=%d) but is mandatory! class '%s'.", fName,
                String.valueOf(value), elementLength, cName));
        }

        return value.length() > 0 ? value : null;
    }

    /**
     * Get all repetitions of a composite. A entry can be defined a composite
     * OR it can be a repeating group of composites.
     *
     * @param field the Composite OR group of composites.
     * @param listObj the object that has the composite.
     * @return A List with n:th Composite(s)
     */
    public List<Composite> getCompositeList(Field field, Object listObj) {

        String cName = listObj.getClass().getSimpleName();
        String fName = field.getName();
        EdiComposite ediSegment = field.getAnnotation(EdiComposite.class);
        int repMax = ediSegment.repMax();
        int repMin = ediSegment.repMin();

        listObj = getObject(field, listObj);

        if (!(listObj instanceof List)) {

            throw new ParserException(
                String
                    .format(
                        "Error: Definition of annotated EdiSegment. Field '%s' class '%s'. Only types of <java.util.List> is allowed!",
                        fName, cName));
        }

        LOG.debug(String.format("Field '%s' (%d/%d) class '%s'", fName, repMin, repMax, cName));

        @SuppressWarnings("unchecked")
        List<Composite> compositeList = (List<Composite>) listObj;

        return compositeList;
    }

    /**
     * Get field object
     *
     * @param field the Active field.
     * @param listObj the object that has the field.
     * @return Object
     */
    private Object getObject(Field field, Object listObj) {

        String cName = listObj.getClass().getSimpleName();
        String fName = field.getName();
        Object obj = null;

        try {

            obj = field.get(listObj);
        }
        catch (IllegalArgumentException | IllegalAccessException ex) {

            String errMsg = String.format("Error: Accessing object on: '%s' class '%s'.", fName, cName);

            LOG.error(errMsg, ex);

            throw new ParserException(errMsg, ex);
        }

        return obj;
    }

    /**
     * Check repetitions against definitions
     *
     * @param field the Active field to validate against the annotations.
     * @param compositeList list of composites available.
     * @return true if ok.
     * @throws ParserException if repetitions differs against definitions
     */
    public boolean checkCompositeRepetitions(Field field, List<Composite> compositeList) {

        EdiComposite ediSegment = field.getAnnotation(EdiComposite.class);
        String cName = compositeList.getClass().getSimpleName();
        String fName = field.getName();
        int repMax = ediSegment.repMax();
        int repMin = ediSegment.repMin();

        // Check repetitions
        if (compositeList.size() < repMin) {

            throw new ParserException(String.format(
                "Repetitions error: min rep. is %d, actually rep. is %d for field '%s' class '%s'.", repMin,
                compositeList.size(), fName, cName));
        }
        else if (compositeList.size() > repMax) {

            throw new ParserException(String.format(
                "Repetitions error: max rep. is %d, actually rep. is %d for field '%s' class '%s'.", repMax,
                compositeList.size(), fName, cName));
        }

        return true;
    }
}