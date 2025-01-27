package br.com.uniquedata.restfull.sdk.annotation.simple;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * Annotation which indicates that a method attribute form data 
 * should be bound to a web request attribute form data.
 *
 * <p>Example URL [POST] http://domain.com.br/endpoint
 * 
 * <ul><strong>Some examples of RestFullMediaType</strong>
 *  <li>MULTIPART_FORM_DATA : multipart/form-data</li> 
 *  <li>APPLICATION_X_WWW_FORM_URLENCODED : application/x-www-form-urlencoded</li> 
 * </ul>
 * 
 * <pre>
"@UniqueDataRestFullPost(endpoint = "/save", 
accept = RestFullMediaType.APPLICATION_X_WWW_FORM_URLENCODED)
public TestResponseBodyDto getById(@RestFullFormData("name") final String name);"
 </pre>
 *
 * 
 * @author Jaderson Berti
 * @author Unique Data Inovatation (company)
 * @since 1.0
 * @see RestFullParam
 * @see RestFullPathVar
 *
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestFullFormData {
	
	String value();

}
