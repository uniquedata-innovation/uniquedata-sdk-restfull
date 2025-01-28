package br.com.uniquedata.restfull.sdk.annotation.simple;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * Annotation indicates that you can create an object with the url parameter fields.
 * 
 * <p>Supported for annotated handler methods.
 * 
 * <p>Example of URL https://dominio.com.br/endpoint/filters?name=jader&text=dev&complex_name=jb
 *
 * <p> Example of Object
 * <pre>
public class TestRequestObjectToParamDto {
  private String name;
  private String text;
  @RestFullField("complex_name")
  private String complexName;
}
"@UniqueDataRestGet("/filters")
public TestResponseBodyDto filters(@RestFullObjectToParam final TestRequestObjectToParamDto object);"	
 </pre>
 * 
 * @author Jaderson Berti
 * @author Unique Data Inovatation (company)
 * @since 1.0
 * @see RestFullBody
 * @see RestFullMapToParam
 * @see RestFullFormData
 * @see RestFullObjectToFormData
 *
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestFullObjectToParam {

}
