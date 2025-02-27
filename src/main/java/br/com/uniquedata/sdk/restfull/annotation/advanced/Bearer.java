package br.com.uniquedata.sdk.restfull.annotation.advanced;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * Annotation indicates the bearer token reponse
 * 
 * @author Jaderson Berti
 * @author Unique Data Inovatation (company)
 * @since 1.0
 *
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Bearer {

}
