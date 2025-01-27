package br.com.uniquedata.restfull.sdk.annotation.simple;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * Annotation which indicates that a method <strong>variable</strong> should be bound to a web
 * request <strong>path var</strong>.
 *
 * <p>Example URL http://domain.com.br/endpoint/200
 *
 * <pre>
"@UniqueDataRestFullGet("/{id}")
public TestResponseBodyDto getById(@RestFullPathVar("id") final String id);"
 
"@UniqueDataRestFullDelete("/{id}")
public TestResponseBodyDto deleteById(@RestFullPathVar("id") final String id);"
 </pre>
 * 
 * @author Jaderson Berti
 * @author Unique Data Inovatation (company)
 * @since 1.0
 * @see RestFullParam
 * @see RestFullFormData
 *
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestFullPathVar {
	
	String value();

}
