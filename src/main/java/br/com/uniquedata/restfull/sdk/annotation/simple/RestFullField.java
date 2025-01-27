package br.com.uniquedata.restfull.sdk.annotation.simple;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * Annotation indicates the actual name of the field in the JSON
 * 
 * Default value ("") indicates that the field name is used
 * as the property name without any modifications, but it
 * can be specified to non-empty value to specify different
 * name. Property name refers to name used externally, as
 * the field name in JSON objects.
 * 
 * @author Jaderson Berti
 * @author Unique Data Inovatation (company)
 * @since 1.0
 *
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestFullField {

    /**
     * Defines name of the actual property, i.e. JSON object field
     * name to use for the property. If value is empty String (which is the
     * default), will try to use name of the field that is annotated.
     * Note that there is
     * <b>no default name available for constructor arguments</b>,
     */
	String value();
	
}
