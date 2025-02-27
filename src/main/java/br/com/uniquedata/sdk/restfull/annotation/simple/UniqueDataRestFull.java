package br.com.uniquedata.sdk.restfull.annotation.simple;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * Annotation for mapping web requests onto methods in request-handling classes
 * with flexible method signatures.
 * 
 * <ul>
 * 	<li>RestFullMethod
 *     <ul>
 *     	<li>
 *     	<pre>
public enum RestFullMethod {
	GET, POST, PUT, DELETE, PATH, OPTION
}
 *      </pre>
 *      </li>
 *     </ul>
 *  </li>
 *  <li>
 *   RestFullMethod
 *     <ul>
 *     	<li>RestFullMediaType
 *      <pre>
public enum RestFullMediaType {
	APPLICATION_JSON("application/json"),	
	APPLICATION_XML("application/xml"),	
	APPLICATION_OCTET_STREAM("application/octet-stream"),	
	APPLICATION_X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded"),		
	MULTIPART_FORM_DATA("multipart/form-data");
}
 *      </pre>
 *     </li>
 *     </ul>
 *  </li>
 * 
 * </ul>
 * 
 * <pre>
"@UniqueDataRestFull(method = RestFullMethod.GET, endpoint = "/all")
public List<TestResponseBodyDto> findAll();"

"@UniqueDataRestFull(method = RestFullMethod.POST, endpoint = "/save",
accept = RestFullMediaType.APPLICATION_X_WWW_FORM_URLENCODED)
public TestResponseBodyDto saveByUrlEncoded(@RestFullFormData("name") final String name);"
 </pre>
 * 
 * @author jadersonberti
 * @author UniqueData Inovatation (company)
 * @since 1.0
 * @see UniqueDataRestFullGet
 * @see UniqueDataRestFullPost
 * @see UniqueDataRestFullPut
 * @see UniqueDataRestFullPath
 * @see UniqueDataRestFullDelete
 * @see UniqueDataRestFullOpition
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueDataRestFull {
	
	/**
	 * The name of path the request to bind to.
	 */
	String endpoint();
	
	/**
	 * The HTTP request methods to map to, narrowing the primary mapping:
	 *  <pre>
public enum RestFullMethod {
	GET, POST, PUT, DELETE, PATH, OPTION
}
 	 * </pre>
	 */
	RestFullMethod method();

	/**
	 * The accept media types of the mapped request, narrowing the primary mapping.
	 * <p>The format is a single media type or a sequence of media types.
	 * Examples:
	 * <pre>
public enum RestFullMediaType {
	APPLICATION_JSON("application/json"),	
	APPLICATION_XML("application/xml"),	
	APPLICATION_OCTET_STREAM("application/octet-stream"),	
	APPLICATION_X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded"),		
	MULTIPART_FORM_DATA("multipart/form-data");
}
	 * </pre>
	 * 
	 *< strong>accept default: RestFullContentType.APPLICATION_JSON</strong>
	 */
	RestFullMediaType accept() default RestFullMediaType.APPLICATION_JSON;

	/**
	 * The producible media types of the mapped request, narrowing the primary mapping.
	 * <p>The format is a single media type or a sequence of media types,
	 * Examples:
	 * <pre>
public enum RestFullMediaType {
	APPLICATION_JSON("application/json"),	
	APPLICATION_XML("application/xml"),	
	APPLICATION_OCTET_STREAM("application/octet-stream"),	
	APPLICATION_X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded"),		
	MULTIPART_FORM_DATA("multipart/form-data");
}
	 * </pre>
	 * <p>It affects the actual content type written, for example to produce a JSON response.
	 * 
	 *<strong>content type default: RestFullMediaType.APPLICATION_JSON</strong>
	 */
	RestFullMediaType contentType() default RestFullMediaType.APPLICATION_JSON;
	
	public enum RestFullMethod {
		GET, POST, PUT, DELETE, PATH, OPTION
	}
	
	public enum RestFullMediaType {
		APPLICATION_JSON("application/json"),	
		APPLICATION_XML("application/xml"),	
		APPLICATION_OCTET_STREAM("application/octet-stream"),	
		APPLICATION_X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded"),		
		MULTIPART_FORM_DATA("multipart/form-data");
		
		private String type;
		
		RestFullMediaType(final String type) {
			this.type = type;
		}
		
		public String getType() {
			return type;
		}
	}

}