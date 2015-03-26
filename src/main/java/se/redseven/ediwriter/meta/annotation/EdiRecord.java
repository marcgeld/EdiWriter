package se.redseven.ediwriter.meta.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EdiRecord {

    int repMin();

    int repMax();

}