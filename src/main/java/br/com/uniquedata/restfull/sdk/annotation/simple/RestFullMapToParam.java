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
Map<String, Object> atributeParameters = ...;
atributeFormData.put("first","JADER");
atributeFormData.put("last","DEV");

"@UniqueDataRestGet(endpoint = "/parameters",accept)
public TestResponseBodyDto get(@RestFullMapToParam final Map<String, Object> parameters);"

build to > /paramters?first=JADER&last=BERTI
 </pre>
 * 
 * @author Jaderson Berti
 * @author Unique Data Inovatation (company)
 * @since 1.0
 * @see RestFullBody
 * @see RestFullObjectToParam
 * @see RestFullFormData
 * @see RestFullObjectToFormData
 *
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestFullMapToParam {

}
