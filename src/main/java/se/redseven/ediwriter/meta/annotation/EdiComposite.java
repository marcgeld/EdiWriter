package se.redseven.ediwriter.meta.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * EdiComposite interface.
 * @author ICC
 * RetentionPolicy.RUNTIME == keep annotations to Java runtime.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface EdiComposite {

    /**
     * Get min repetitions (defaults to 0).
     * @return repMin
     */
    int repMin() default 0;

    /**
     * Get max repetitions (defaults to 1).
     * @return repMax
     */
    int repMax() default 1;

}
