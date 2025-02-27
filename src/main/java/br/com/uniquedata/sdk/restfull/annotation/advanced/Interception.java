package br.com.uniquedata.sdk.restfull.annotation.advanced;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation that defines if interception is applied automatically, as well as
 * additional headers for those intercepted requests.
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Interception {
	
	/**
     * Defines if automatic interception is enabled.
     */
	boolean enabled();
	
	/**
     * Set expiration in milliseconds manually.
     * 
     * <p>
     * 
     * The milliseconds will be added to the current date and an expireDate will be generated.
     */
	long expireInMilliseconds() default -1;
	
	
    /**
     * Additional headers for all request that should be included only when autoInterception is enabled.
     */
	AdditionalHeader[] additionalHeaders() default {};

}
