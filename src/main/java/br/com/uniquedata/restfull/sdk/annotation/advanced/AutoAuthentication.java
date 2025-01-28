package br.com.uniquedata.restfull.sdk.annotation.advanced;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.uniquedata.restfull.sdk.annotation.simple.RestFullFormData;
import br.com.uniquedata.restfull.sdk.annotation.simple.RestFullObjectToFormData;
import br.com.uniquedata.restfull.sdk.annotation.simple.RestFullMapToParam;
import br.com.uniquedata.restfull.sdk.annotation.simple.RestFullObjectToParam;

/**
 * 
 * <p> Annotation indicating that the API will perform authentication automatically.
 * 
 * <p>
 * Use this annotation on classes that need to perform automatic authentication
 * (e.g., obtaining and refreshing tokens) for API requests.
 * </p>
 * 
 * <pre>
{@literal @}AutoAuthentication(
	autoRecover = true,
    type = AuthType.BEARER_TOKEN,
    interception = {@literal @}Interception(
      enabled = true,
      additionalHeaders = {
          {@literal @}AdditionalHeader(name = "Intercept-Header", value = "InterceptValue")
      }
    ),
    authenticate = {@literal @}Authentication(
      authUrl = "https://example.com/api/auth",
      credentialJsonEnvVar = "MY_CREDENTIAL_ENV",
      
      "OR"
      
      credentialJsonForTest = "{\"user\":\"admin\",\"pass\":\"123\"}"
    )
  )
 public interface MyService {
      // Your service methods here
 }
 * </pre>
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
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoAuthentication {
	
	/**
	 * 
	 * Indicates whether the framework should store and recover the token from disk
     * to avoid re-authenticating on each run.
	 * 
	 * <p>
	 * 
	 * If enabled, the token will be persisted to disk after the first successful
	 * authentication. On subsequent runs or when the token is not present in
	 * memory, the framework will attempt to read and validate the token from the
	 * stored file before making a new authentication request.
	 * <p>
	 * This approach helps avoid unnecessary login requests by reusing a valid token
	 * across application restarts or other events. However, it's important to
	 * consider secure storage options (e.g., encrypting the token, restricting file
	 * permissions) to prevent unauthorized access.
	 */
	boolean autoRecover();
	
    /**
     * Defines the type of authentication to be performed.
     */
	AuthType type();
	
	/**
	 * Provides interception settings for automatically applying additional
	 * headers or other request-interception logic.
	 */
	Interception interception() default @Interception(enabled = false);
	
	/**
	 * Defines the authentication configurations such as credentials,
	 * environment variables, or test credentials needed to obtain
	 * or refresh the authentication token.
	 */
	Authentication authenticate() default @Authentication(
		enabled = false, 
		fullUrlAuth = "", 
		typeClassCredential = Void.class,
		typeClassAuthorize = Void.class
	);
	
	/**
     * Types of authentication supported by the framework.
     */
	public enum AuthType {
		BEARER_TOKEN, MANUALLY
	}
	
}