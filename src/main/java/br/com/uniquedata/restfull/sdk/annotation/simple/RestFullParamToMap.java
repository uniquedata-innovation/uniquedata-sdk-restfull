package br.com.uniquedata.restfull.sdk.annotation.simple;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * Annotation indicates that you can create an object with the map request paramater.
 * 
 * <p>Supported for annotated handler methods.
 * 
 * <p> Exemple:
 *
 * <pre>
Map<String, Object> atributeFormData = ...;
atributeFormData.put("first","JADER");
atributeFormData.put("last","BERTI");

"@UniqueDataRestGet(endpoint = "/paramters",accept)
public TestResponseBodyDto get(@RestFullFormDataToMap final Map<String, Object> paramters);"

build to > /paramters?first=JADER&last=BERTI
 </pre>
 * 
 * @author Jaderson Berti
 * @author Unique Data Inovatation (company)
 * @since 1.0
 * @see RestFullBody
 * @see RestFullParamToObject
 * @see RestFullFormData
 * @see RestFullFormDataToObject
 *
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestFullParamToMap {

}
