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
 * <p>Example of URL https://dominio.com.br/endpoint/paramters?name=Jaderson&text=Berti&complex_name=JB
 *
 * <p> Example of Object
 * <pre>
public class TestRequestParamToObjetoDto {
  private String name;
  private String text;
  @RestFullField("complex_name")
  private String complexName;
}
 </pre>
 * 
 * @author Jaderson Berti
 * @author Unique Data Inovatation (company)
 * @since 1.0
 * @see RestFullBody
 * @see RestFullParamToMap
 * @see RestFullFormData
 * @see RestFullFormDataToObject
 *
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestFullParamToObject {

}
