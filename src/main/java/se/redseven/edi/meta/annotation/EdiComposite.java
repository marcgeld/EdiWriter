package se.redseven.edi.meta.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EdiComposite {

    int repMin() default 0;

    int repMax() default 1;

}
