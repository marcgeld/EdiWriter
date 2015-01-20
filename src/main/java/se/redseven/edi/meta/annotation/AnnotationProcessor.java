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
import se.redseven.edi.utils.EdiParserException;

public class AnnotationProcessor implements Constants {

    private static final Logger LOG = LoggerFactory.getLogger(AnnotationProcessor.class);
    private List<String> TEMPLATE_TEXT_LIST = Arrays.asList(Constants.TEMPLATE_TEXT);

    /**
     *
     * @param obj
     * @return
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
            else if (field.isAnnotationPresent(EdiElement.class)) {

                // Get Value
                String elementValue = getElementValue(field, obj);

                if (null != elementValue) {

                    //System.out.println("EV------> " + elementValue);
                    recordList.add(new Element(elementValue));
                }
            }
        }

        return recordList;
    }

    /**
     * Get values from an element
     *
     * @param Element field
     * @param Object
     * @return A List with n:th Composite(s)
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

                throw new EdiParserException(String.format(
                    "Error: Element '%s' to long, value='%s' (lenght=%d), max=%d class '%s'.", fName, value,
                    value.length(), elementLength, cName));
            }
        }

        // Check if Mandatory
        if (elementMandatory && value.trim().equals("")) {

            throw new EdiParserException(String.format(
                "Error: Element '%s' has no value '%s' (lenght=%d) but is mandatory! class '%s'.", fName,
                String.valueOf(value), elementLength, cName));
        }

        return value.length() > 0 ? value : null;
    }

    /**
     * Get all repetitions of a composite. A entry can be defined a composite
     * OR it can be a repeating group of composites.
     *
     * @param field - the Composite OR group of composites.
     * @param listObj
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

            throw new EdiParserException(
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
     * @param field
     * @param listObj
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

            throw new EdiParserException(errMsg, ex);
        }

        return obj;
    }

    /**
     * Check repetitions against definitions
     *
     * @param field
     * @param compositeList
     * @return true if ok.
     * @throws EdiParserException if repetitions differs against definitions
     */
    public boolean checkCompositeRepetitions(Field field, List<Composite> compositeList) {

        EdiComposite ediSegment = field.getAnnotation(EdiComposite.class);
        String cName = compositeList.getClass().getSimpleName();
        String fName = field.getName();
        int repMax = ediSegment.repMax();
        int repMin = ediSegment.repMin();

        // Check repetitions
        if (compositeList.size() < repMin) {

            throw new EdiParserException(String.format(
                "Repetitions error: min rep. is %d, actually rep. is %d for field '%s' class '%s'.", repMin,
                compositeList.size(), fName, cName));
        }
        else if (compositeList.size() > repMax) {

            throw new EdiParserException(String.format(
                "Repetitions error: max rep. is %d, actually rep. is %d for field '%s' class '%s'.", repMax,
                compositeList.size(), fName, cName));
        }

        return true;
    }
}