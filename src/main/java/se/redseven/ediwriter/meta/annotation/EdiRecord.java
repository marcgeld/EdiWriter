package se.redseven.ediwriter.meta.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * EdiRecord interface.
 * @author ICC
 * RetentionPolicy.RUNTIME == keep annotations to Java runtime.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface EdiRecord {

    /**
     * Get min repetitions.
     * @return repMin
     */
    int repMin();

    /**
     * Get max repetitions.
     * @return repMax
     */
    int repMax();

}
