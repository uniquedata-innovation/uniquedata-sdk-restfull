package br.com.uniquedata.sdk.restfull.annotation.advanced;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation defining a single header (key-value) to be added to requests.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface AdditionalHeader {

	/**
     * Header name to include in the request.
     */
    String headerName();

    /**
     * Header value to include in the request.
     */
    String headerValue();

}
