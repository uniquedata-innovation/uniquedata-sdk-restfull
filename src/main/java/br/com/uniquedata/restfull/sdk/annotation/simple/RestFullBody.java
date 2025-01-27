package br.com.uniquedata.restfull.sdk.annotation.simple;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * Annotation indicating a method parameter should be bound to the body of the web request.
 * 
 * <p>Supported for annotated handler methods.
 * 
 * @author Jaderson Berti
 * @author Unique Data Inovatation (company)
 * @since 1.0
 * @see RestFullParamToObject
 * @see RestFullParamToMap
 * @see RestFullFormData
 * @see RestFullFormDataToObject
 *
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestFullBody {

}
