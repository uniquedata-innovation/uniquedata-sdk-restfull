package br.com.uniquedata.sdk.restfull.annotation.simple;

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
 * @see RestFullObjectToParam
 * @see RestFullMapToParam
 * @see RestFullFormData
 * @see RestFullObjectToFormData
 *
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestFullBody {

}
