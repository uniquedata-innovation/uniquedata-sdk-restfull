package br.com.uniquedata.restfull.sdk.annotation.simple;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * Annotation indicates that you can create an object with the map atribute form data.
 * 
 * <p>Supported for annotated handler methods.
 * 
 * <p> Exemple:
 *
 * <pre>
Map<String, Object> atributeFormData = ...;
atributeFormData.put("fist","JADER");
atributeFormData.put("last","BERTI");

"@UniqueDataRestPost(endpoint = "/",accept = RestFullMediaType.APPLICATION_X_WWW_FORM_URLENCODED)
public TestResponseBodyDto save(@RestFullFormDataToMap final Map<String, Object> formaDataMa);"	
 </pre>
 * 
 * @author Jaderson Berti
 * @author Unique Data Inovatation (company)
 * @since 1.0
 * @see RestFullBody
 * @see RestFullParamToObject
 * @see RestFullParamToMap
 * @see RestFullFormData
 * @see RestFullFormDataToObject
 *
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestFullFormDataToMap {

}
