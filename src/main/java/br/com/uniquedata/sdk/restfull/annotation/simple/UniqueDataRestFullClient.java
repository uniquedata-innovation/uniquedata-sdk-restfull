package br.com.uniquedata.sdk.restfull.annotation.simple;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Service;

/**
 * Annotation for route request and location method of class for request map.
 * 
 * <pre>
"@UniqueDataRestFullClient(baseUrl = "https://webservice.com.br/")
public interface TestApi {

"@UniqueDataRestFullGet("/product/details")
public List<TestResponseBodyDto> get(@RestFullParamToObject final ParamterToObjetoTestDto dto);"

}"
 * </pre>
 * 
 * @author Jaderson Berti
 * @author Unique Data Inovatation (company)
 * @since 1.0
 * 
 */
@Service
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueDataRestFullClient {
	
	/**
	 * The name of base url the request to bind to.
	 */
	String baseUrl() default "";
	
	/**
	 * Monitors endpoints for automatic authentication using the @AutoAuthentication annotation.
	 * 
	 * <p>
	 * When enabled, this class monitors the specified service endpoint and automatically
	 * performs authentication by setting the "Authorization" header.
	 * </p>
	 * 
	 * @return The endpoint class to be monitored for automatic authentication.
	 */
	Class<?> autoAuthEndpointMonitor() default Void.class;
	
}