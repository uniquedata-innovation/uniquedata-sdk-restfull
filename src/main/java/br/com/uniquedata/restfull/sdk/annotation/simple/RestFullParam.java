package br.com.uniquedata.restfull.sdk.annotation.simple;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * Annotation which indicates that a method parameter should be bound to a web
 * request parameter.
 *
 * <p>Example URL http://domain.com.br/endpoint?id=22
 *
 * <pre>
"@UniqueDataRestFullGet("/")
public TestResponseBodyDto getById(@RestFullParam("id") final String id);"
 </pre>
 *
 *
 * @author Jaderson Berti
 * @author Unique Data Inovatation (company)
 * @since 1.0
 * @see RestFullFormData
 * @see RestFullPathVar
 *
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestFullParam {
	
	String value();
	
}
