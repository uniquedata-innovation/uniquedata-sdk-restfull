package br.com.uniquedata.restfull.sdk.implementation.beanbuild;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.uniquedata.restfull.sdk.annotation.simple.RestFullBody;
import br.com.uniquedata.restfull.sdk.annotation.simple.RestFullField;
import br.com.uniquedata.restfull.sdk.annotation.simple.RestFullFormData;
import br.com.uniquedata.restfull.sdk.annotation.simple.RestFullFormDataToMap;
import br.com.uniquedata.restfull.sdk.annotation.simple.RestFullFormDataToObject;
import br.com.uniquedata.restfull.sdk.annotation.simple.RestFullParam;
import br.com.uniquedata.restfull.sdk.annotation.simple.RestFullParamToObject;
import br.com.uniquedata.restfull.sdk.annotation.simple.RestFullPathVar;
import br.com.uniquedata.restfull.sdk.annotation.simple.UniqueDataRestFull;
import br.com.uniquedata.restfull.sdk.annotation.simple.UniqueDataRestFull.RestFullMediaType;
import br.com.uniquedata.restfull.sdk.annotation.simple.UniqueDataRestFull.RestFullMethod;
import br.com.uniquedata.restfull.sdk.annotation.simple.UniqueDataRestFullDelete;
import br.com.uniquedata.restfull.sdk.annotation.simple.UniqueDataRestFullGet;
import br.com.uniquedata.restfull.sdk.annotation.simple.UniqueDataRestFullOpition;
import br.com.uniquedata.restfull.sdk.annotation.simple.UniqueDataRestFullPatch;
import br.com.uniquedata.restfull.sdk.annotation.simple.UniqueDataRestFullPost;
import br.com.uniquedata.restfull.sdk.annotation.simple.UniqueDataRestFullPut;
import br.com.uniquedata.restfull.sdk.helper.ObjectReflectionHelper;
import br.com.uniquedata.restfull.sdk.implementation.clientbuild.UniqueDataRestFullWebClientBuild;

/**
 * 
 * Class that can be used to bootstrap and launch a Spring application from a Java main
 * method. By default class will perform the following steps to bootstrap your
 * application:
 * 
 * * @author jadersonberti by company UNIQUE DATA INOVATATION
 *
 */
public class UniqueDataRestFullBuiderBeanInvocationHandler implements InvocationHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UniqueDataRestFullBuiderBeanInvocationHandler.class);

	private String baseUrl;
	
	private Class<?> webClientKey;
	
	public UniqueDataRestFullBuiderBeanInvocationHandler(final String baseUrl, final Class<?> webClientKey) {
		this.baseUrl = baseUrl;
		this.webClientKey = webClientKey;
	}
	
	@Override
	public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
		if(method.isAnnotationPresent(UniqueDataRestFull.class)) {
			final UniqueDataRestFull annotation = method.getAnnotation(UniqueDataRestFull.class);
			return webClientBuild(method, args, annotation.endpoint(), annotation.contentType(), annotation.accept(), annotation.method());
		}
		
		if(method.isAnnotationPresent(UniqueDataRestFullGet.class)) {
			final UniqueDataRestFullGet annotation = method.getAnnotation(UniqueDataRestFullGet.class);
			return webClientBuild(method, args, annotation.value(), null, annotation.accept(), RestFullMethod.GET);
		}
		
		if(method.isAnnotationPresent(UniqueDataRestFullPost.class)) {
			final UniqueDataRestFullPost annotation = method.getAnnotation(UniqueDataRestFullPost.class);
			return webClientBuild(method, args, annotation.value(), annotation.contentType(), annotation.accept(), RestFullMethod.POST);
		}
		
		if(method.isAnnotationPresent(UniqueDataRestFullPut.class)) {
			final UniqueDataRestFullPut annotation = method.getAnnotation(UniqueDataRestFullPut.class);
			return webClientBuild(method, args, annotation.value(), annotation.contentType(), annotation.accept(), RestFullMethod.PUT);			
		}
		
		if(method.isAnnotationPresent(UniqueDataRestFullPatch.class)) {
			final UniqueDataRestFullPatch annotation = method.getAnnotation(UniqueDataRestFullPatch.class);
			return webClientBuild(method, args, annotation.value(), annotation.contentType(), annotation.accept(), RestFullMethod.PATH);
		}
		
		if(method.isAnnotationPresent(UniqueDataRestFullDelete.class)) {
			final UniqueDataRestFullDelete annotation = method.getAnnotation(UniqueDataRestFullDelete.class);
			return webClientBuild(method, args, annotation.value(), null, annotation.accept(), RestFullMethod.DELETE);
		}
		
		if(method.isAnnotationPresent(UniqueDataRestFullOpition.class)) {
			final UniqueDataRestFullOpition annotation = method.getAnnotation(UniqueDataRestFullOpition.class);
			return webClientBuild(method, args, annotation.value(), null, annotation.accept(), RestFullMethod.OPTION);
		}
		
		LOGGER.error("Annotation [UniqueDataRestFull + Protocol] not present");
		throw new RuntimeException("Annotation [UniqueDataRestFull + Protocol] not present");
	}
	
	private Object webClientBuild(final Method method, final Object[] args, String endpoint, 
		final RestFullMediaType contentType, final RestFullMediaType acceptType, final RestFullMethod restFullMethod) {
		
		final Map<Class<?>, Map<String, Object>> requestSettings = createRequestSettings(method.getParameters(), args);
		final UniqueDataRestFullWebClientBuild webClientBuild = new UniqueDataRestFullWebClientBuild();
		
		endpoint = formatEndpointUrl(endpoint);
		endpoint = buildRestFullParam(endpoint, requestSettings.get(RestFullParam.class));
		endpoint = buildRestFullPathVar(endpoint, requestSettings.get(RestFullPathVar.class));
		endpoint = buildRestFullParamBody(endpoint, requestSettings.get(RestFullParamToObject.class));
		
		String restFullFormData = new String();
		restFullFormData = buildRestFullFormData(restFullFormData, requestSettings.get(RestFullFormData.class));
		restFullFormData = buildRestFullFormDataToObject(restFullFormData, requestSettings.get(RestFullFormDataToObject.class));
		restFullFormData = buildRestFullFormDataToMap(restFullFormData, requestSettings.get(RestFullFormDataToMap.class));
		
		final Map<String, Object> restFullBody = requestSettings.get(RestFullBody.class);
		final Type genericReturnType = method.getGenericReturnType() != null ? method.getGenericReturnType() : Void.class;
		
		if(restFullBody.containsKey(RestFullBody.class.getName())) {
			final Object requestBody = restFullBody.get(RestFullBody.class.getName());
			return webClientBuild.build(endpoint, requestBody, restFullMethod, contentType, acceptType, genericReturnType, webClientKey);
		}else if(restFullFormData != null && !restFullFormData.isEmpty()) {
			return webClientBuild.build(endpoint, restFullFormData, restFullMethod, contentType, acceptType, genericReturnType, webClientKey);
		}else {
			return webClientBuild.build(endpoint, null, restFullMethod, contentType, acceptType, genericReturnType, webClientKey);
		}
	}
	
	private String buildRestFullPathVar(String endpoint, final Map<String, Object> restFullPathVars) {
		for (final Entry<String, Object> restFullPathVar : restFullPathVars.entrySet()) {
			endpoint = endpoint.replaceAll("\\{"+restFullPathVar.getKey()+"\\}", restFullPathVar.getValue().toString());
		}
		
		return endpoint;
	}
	
	private String buildRestFullParam(String endpoint, final Map<String, Object> restFullParams) {
		for (final Entry<String, Object> restFullParam : restFullParams.entrySet()) {
			if(endpoint.contains("?")) {
				endpoint = String.format("%s&%s=%s", endpoint, restFullParam.getKey(), restFullParam.getValue());
			}else {
				endpoint = String.format("%s?%s=%s", endpoint, restFullParam.getKey(), restFullParam.getValue());
			}
		}
		
		return endpoint;
	}
	
	private String buildRestFullFormData(String restFullFormDataQuery, final Map<String, Object> restFullFormData) {
		for (final Entry<String, Object> parameter : restFullFormData.entrySet()) {
			if(restFullFormDataQuery == null || restFullFormDataQuery.isEmpty()) {
				restFullFormDataQuery += String.format("%s=%s", parameter.getKey(), parameter.getValue());
			}else{
				restFullFormDataQuery += String.format("&%s=%s", parameter.getKey(), parameter.getValue());
			}
		}
		
		return restFullFormDataQuery;
	}
	
	private String buildRestFullParamBody(String endpoint, final Map<String, Object> restFullParamBody) {
		if(!restFullParamBody.isEmpty()) {
			final Object object = restFullParamBody.get(RestFullParamToObject.class.getName());
			final Map<String, Object> parameters = ObjectReflectionHelper.getFieldNameAndValue(object, RestFullField.class);
			
			for (final Entry<String, Object> parameter : parameters.entrySet()) {
				if(endpoint.contains("?")) {
					endpoint = String.format("%s&%s=%s", endpoint, parameter.getKey(), parameter.getValue());
				}else {
					endpoint = String.format("%s?%s=%s", endpoint, parameter.getKey(), parameter.getValue());
				}
			}
		}
		
		return endpoint;
	}
	
	private String buildRestFullFormDataToObject(String buildingFormData, final Map<String, Object> restFullFormDataToObject) {
		if(!restFullFormDataToObject.isEmpty()) {
			final Object object = restFullFormDataToObject.get(RestFullFormDataToObject.class.getName());
			final Map<String, Object> parameters = ObjectReflectionHelper.getFieldNameAndValue(object, RestFullField.class);
			
			for (final Entry<String, Object> parameter : parameters.entrySet()) {
				if(buildingFormData == null || buildingFormData.isEmpty()) {
					buildingFormData += String.format("%s=%s", parameter.getKey(), parameter.getValue());
				}else{
					buildingFormData += String.format("&%s=%s", parameter.getKey(), parameter.getValue());
				}
			}
		}
		
		return buildingFormData;
	}
	
	private String buildRestFullFormDataToMap(String buildingFormData, final Map<String, Object> restFullFormDataToMap) {
		for (final Entry<String, Object> parameter : restFullFormDataToMap.entrySet()) {
			if(buildingFormData == null || buildingFormData.isEmpty()) {
				buildingFormData += String.format("%s=%s", parameter.getKey(), parameter.getValue());
			}else{
				buildingFormData += String.format("&%s=%s", parameter.getKey(), parameter.getValue());
			}
		}
		
		return buildingFormData;
	}
	
	@SuppressWarnings("unchecked")
	private Map<Class<?>, Map<String, Object>> createRequestSettings(final Parameter[] parameters, final Object[] args) {
		final Map<Class<?>, Map<String, Object>> requestSettings = new HashMap<>();
		
		final Map<String, Object> pathVar = new HashMap<>();
		requestSettings.put(RestFullPathVar.class, pathVar);
		
		final Map<String, Object> requestParam = new HashMap<>();
		requestSettings.put(RestFullParam.class, requestParam);
		
		final Map<String, Object> requestBody = new HashMap<>();
		requestSettings.put(RestFullBody.class, requestBody);
		
		final Map<String, Object> requestParamBody = new HashMap<>();
		requestSettings.put(RestFullParamToObject.class, requestParamBody);
		
		final Map<String, Object> requestFormData = new HashMap<>();
		requestSettings.put(RestFullFormData.class, requestFormData);
		
		final Map<String, Object> requestFormDataToObject = new HashMap<>();
		requestSettings.put(RestFullFormDataToObject.class, requestFormDataToObject);
		
		requestSettings.put(RestFullFormDataToMap.class, new HashMap<>());
		
		for (int i = 0; i < parameters.length; i++) {
			final Object object = args[i];
			final Parameter parameter = parameters[i];
			
			if(parameter.isAnnotationPresent(RestFullPathVar.class)) {
				final RestFullPathVar restFullPathVar = parameter.getAnnotation(RestFullPathVar.class);
				pathVar.put(restFullPathVar.value(), object);
			}
			
			if(parameter.isAnnotationPresent(RestFullParam.class)) {
				final RestFullParam restFullParam = parameter.getAnnotation(RestFullParam.class);
				requestParam.put(restFullParam.value(), object);
			}
			
			if(parameter.isAnnotationPresent(RestFullFormData.class)) {
				final RestFullFormData restFullFormData = parameter.getAnnotation(RestFullFormData.class);
				pathVar.put(restFullFormData.value(), object);
			}
			
			if(parameter.isAnnotationPresent(RestFullBody.class)) {
				requestBody.put(RestFullBody.class.getName(), object);
			}
			
			if(parameter.isAnnotationPresent(RestFullParamToObject.class)) {
				requestParamBody.put(RestFullParamToObject.class.getName(), object);
			}
			
			if(parameter.isAnnotationPresent(RestFullFormDataToObject.class)) {
				requestFormDataToObject.put(RestFullFormDataToObject.class.getName(), object);
			}
			
			if(parameter.isAnnotationPresent(RestFullFormDataToMap.class)) {
				requestSettings.put(RestFullFormDataToMap.class, Map.class.cast(object));
			}
		}
		
		return requestSettings;
	}
	
	private String formatEndpointUrl(final String endpoint) {
		final char beginCharEndpoint = endpoint.charAt(0);
		final char lastCharBaseUrl = baseUrl.charAt(baseUrl.length() - 1);
		
		if(lastCharBaseUrl == '/' || beginCharEndpoint == '/') {
			return baseUrl + endpoint;
		}
		
		return baseUrl + "/" + endpoint;
	}

}