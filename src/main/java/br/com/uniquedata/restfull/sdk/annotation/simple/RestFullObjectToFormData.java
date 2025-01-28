package br.com.uniquedata.restfull.sdk.annotation.simple;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * Annotation indicates that you can create an object with the Object atribute form data.
 * 
 * <p>Supported for annotated handler methods.
 * 
 * <p> Example:
 *
 * <pre>
public class TestRequestObjectToFormDataDto {
  private String name;
  private String text;
  @RestFullField("complex_name")
  private String complexName;
}

"@UniqueDataRestPost(endpoint = "/",accept = RestFullMediaType.APPLICATION_X_WWW_FORM_URLENCODED)
public TestResponseBodyDto save(@RestFullObjectToFormData final TestRequestObjectToFormDataDto object);"	
 </pre>
 * 
 * @author Jaderson Berti
 * @author Unique Data Inovatation (company)
 * @since 1.0
 * @see RestFullBody
 * @see RestFullObjectToParam
 * @see RestFullMapToParam
 * @see RestFullFormData
 *
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestFullObjectToFormData {

}
