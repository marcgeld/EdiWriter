package se.redseven.edi.meta.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EdiElement {

    boolean mandatory() default false;

    int length();

    String value() default "";
}
