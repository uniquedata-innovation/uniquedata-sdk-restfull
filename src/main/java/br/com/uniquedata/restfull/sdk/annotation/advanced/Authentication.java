package br.com.uniquedata.restfull.sdk.annotation.advanced;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import br.com.uniquedata.restfull.sdk.annotation.simple.UniqueDataRestFull.RestFullMethod;

/**
 * Annotation indicates config will perform authentication automatically 
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Authentication {

	/**
     * Defines if automatic auto authenticate is enabled.
     */
	boolean enabled();
	
    /**
     * The full URL used to authenticate or refresh tokens, if needed.
     */
    String fullUrlAuth();
    
    /**
     * The Http Method used to authenticate.
     * default by POST.
     * 
     * This config is deprecated. Method Only POST
     */
    @Deprecated
    RestFullMethod method() default RestFullMethod.POST;
    
    /**
     * Additional headers specific request auth that should be included only when autoAuthenticate is enabled.
     */
    AdditionalHeader[] additionalHeaders() default {};
    
    /**
     * Class that represents the credential type required for authentication.
     */
	Class<?> typeClassCredential();
	
	/**
     * Class that represents the response authorize.
     */
	Class<?> typeClassAuthorize();
	
	/**
     * Environment variable name containing the credential JSON.
     * 
     * Example export variable: export AUTH_DATA="{"email":"teste@gmail.com","password":"123"}"
     * 
     * Example credentialJsonEnvironmentVariable = "AUTH_DATA";
     */
	String credentialJsonEnvironmentVariable() default "";

    /**
     * Credential JSON for test environments.
     */
	String credentialJsonForTest() default "";
	
}
