package br.com.uniquedata.sdk.restfull.annotation.simple;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.uniquedata.sdk.restfull.annotation.simple.UniqueDataRestFull.RestFullMediaType;

/**
 * Annotation for mapping HTTP OPTION requests onto specific handler
 * methods.
 *
 * <p>Specifically, {@code UniqueDataRestFullOpition} is a <em>composed annotation</em>.
 *
 * @author Jaderson Berti
 * @author Unique Data Inovatation (company)
 * @since 1.0
 * @see UniqueDataRestFullGet
 * @see UniqueDataRestFullPost
 * @see UniqueDataRestFullPut
 * @see UniqueDataRestFullPath
 * @see UniqueDataRestFullDelete
 * @see UniqueDataRestFullOpition
 * 
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueDataRestFullOpition {
	
	/**
	 * The name of path the request to bind to.
	 */
	String value();

	/**
	 * {@link UniqueDataRestFull#accept()}
	 * 
	 *< strong>accept default: RestFullMediaType.APPLICATION_JSON</strong>
	 */
	RestFullMediaType accept() default RestFullMediaType.APPLICATION_JSON;

}
