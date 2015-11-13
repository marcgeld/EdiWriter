package se.redseven.ediwriter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.redseven.ediwriter.error.ParserException;
import se.redseven.ediwriter.meta.annotation.EdiComposite;
import se.redseven.ediwriter.meta.annotation.EdiElement;

/**
 * Class that process annotation of compiled classes.
 * If classes are added, don't forget to add '@Retention(RetentionPolicy.RUNTIME)' to the class.
 *
 * <p>
 *  It is very important to add the '@Retention(RetentionPolicy.RUNTIME)' annotation.
 *  Without that annotation, the compiler will discard any custom annotation in the class file.
 */
public class AnnotationProcessor implements Constants {

    private static final String REPETITIONS_ERROR_MAX =
        "Repetitions error: max rep. is %d, actually rep. is %d for field '%s' class '%s'.";
    private static final String REPETITIONS_ERROR_MIN =
        "Repetitions error: min rep. is %d, actually rep. is %d for field '%s' class '%s'.";
    private static final String ERROR_ACCESSING_OBJECT = "Error: Accessing object on: '%s' class '%s'.";
    private static final String ERROR_ELEMENT_HAS_NO_VALUE =
        "Error: Element '%s' has no value '%s' (lenght=%d) but is mandatory! class '%s'.";
    private static final String ERROR_ELEMENT_TO_LONG =
        "Error: Element '%s' to long, value='%s' (lenght=%d), max=%d class '%s'.";
    private static final String ERROR_DEFINITION_JAVA_UTIL_LIST_IS_ALLOWED =
        "Error: Definition of annotated EdiSegment. Field '%s' class '%s'. Only types of <java.util.List> is allowed!";
    private static final Logger LOG = LoggerFactory.getLogger(AnnotationProcessor.class);
    private List<String> templateText = Arrays.asList(Constants.TEMPLATE_TEXT);

    /**
     * Get values from an element.
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
        } else {

            value = String.valueOf(elemObj);
        }

        LOG.debug(String.format("Element '%s' value '%s' default value '%s' max-lenght=%d class '%s'.", fName, value,
            defValue, elementLength, cName));

        if (StringUtils.isEmpty(value) && StringUtils.isNotEmpty(defValue)) {

            value = StringUtils.trimToEmpty(value);
        }

        // Check length and possible Exclude from check...
        if ((value.length() > elementLength) && !templateText.contains(value)) {

            throw new ParserException(
                String.format(ERROR_ELEMENT_TO_LONG, fName, value, value.length(), elementLength, cName));
        }

        // Check if Mandatory
        if (elementMandatory && StringUtils.isEmpty(value)) {

            throw new ParserException(String.format(ERROR_ELEMENT_HAS_NO_VALUE, fName, value, elementLength, cName));
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

            throw new ParserException(String.format(ERROR_DEFINITION_JAVA_UTIL_LIST_IS_ALLOWED, fName, cName));
        }

        LOG.debug(String.format("Composite '%s' (%d/%d) class '%s'", fName, repMin, repMax, cName));

        @SuppressWarnings("unchecked")
        List<Composite> compositeList = (List<Composite>) listObj;

        return compositeList;
    }

    /**
     * Get field object.
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
        } catch (IllegalArgumentException ex) {

            String errMsg = String.format(ERROR_ACCESSING_OBJECT, fName, cName);
            LOG.error(errMsg, ex);
            throw new ParserException(errMsg, ex);

        } catch (IllegalAccessException ex) {

            String errMsg = String.format(ERROR_ACCESSING_OBJECT, fName, cName);
            LOG.error(errMsg, ex);
            throw new ParserException(errMsg, ex);
        }

        return obj;
    }

    /**
     * Check repetitions against definitions.
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

            throw new ParserException(String.format(REPETITIONS_ERROR_MIN, repMin, compositeList.size(), fName, cName));

        } else if (compositeList.size() > repMax) {

            throw new ParserException(String.format(REPETITIONS_ERROR_MAX, repMax, compositeList.size(), fName, cName));
        }

        return true;
    }
}