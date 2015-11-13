package se.redseven.ediwriter.meta.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * EdiElement interface.
 * @author ICC
 * RetentionPolicy.RUNTIME == keep annotations to Java runtime.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface EdiElement {

    /**
     * is mandatory (default is false).
     * @return mandatory
     */
    boolean mandatory() default false;

    /**
     * Get lenght.
     * @return length
     */
    int length();

    /**
     * Get value (default is empty string).
     * @return value
     */
    String value() default "";
}
