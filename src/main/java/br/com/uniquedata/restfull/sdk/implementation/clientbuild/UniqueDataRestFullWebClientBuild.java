package br.com.uniquedata.restfull.sdk.implementation.clientbuild;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.codec.DecodingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import br.com.uniquedata.restfull.sdk.annotation.simple.UniqueDataRestFull.RestFullMediaType;
import br.com.uniquedata.restfull.sdk.annotation.simple.UniqueDataRestFull.RestFullMethod;
import br.com.uniquedata.restfull.sdk.exception.UniqueDataRestFullException;
import br.com.uniquedata.restfull.sdk.exception.UniqueDataRestFullException.ExceptionType;
import br.com.uniquedata.restfull.sdk.helper.GenericReturnTypeClassHelper;
import br.com.uniquedata.restfull.sdk.helper.ObjectReflectionHelper;
import br.com.uniquedata.restfull.sdk.pojo.GenericReturnTypeClass;
import br.com.uniquedata.restfull.sdk.pojo.GenericReturnTypeClass.GenericReturnType;
import br.com.uniquedata.restfull.sdk.pojo.ResponseHttpStatus;
import br.com.uniquedata.restfull.sdk.pojo.UniqueDataRestFullResponse;
import reactor.core.publisher.Mono;

public class UniqueDataRestFullWebClientBuild {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(UniqueDataRestFullWebClientBuild.class);
	
	private static final String USER_AGENT = "UniqueDataRestFullSDK/1.0.0";
	
	public Object build(final String uri, final Object requestBody,
			final RestFullMethod restFullMethod, final RestFullMediaType contentyType, 
			final RestFullMediaType acceptType, final Type returnType,  final Class<?> webClientKey) {
		
		return build(uri, requestBody, restFullMethod, contentyType, acceptType, returnType, webClientKey, null, false);
	}
	
	public Object build(final String uri, final Object requestBody, final RestFullMethod restFullMethod, 
		final RestFullMediaType contentyType, final RestFullMediaType acceptType, final Type returnType, 
		final Class<?> webClientKey, final Map<String, String> headers, 
		final boolean isForceUniqueDataRestFullResponse) {
			
		if(isProtocolRequestBody(restFullMethod)) {
			if(isForceUniqueDataRestFullResponse || isUniqueDataRestFullResponse(returnType)) {
				return responseParse(UniqueDataWebClientConfigBuild.getWebClient(webClientKey)
					.method(HttpMethod.valueOf(restFullMethod.name()))
					.uri(uri).headers(toConsumerHttpHeaders(headers))
					.contentType(parseContentType(contentyType, headers))
					.bodyValue(checkBody(requestBody))
					.accept(parseAcceptType(acceptType, headers)), returnType);
			}
			
			return responseParse(UniqueDataWebClientConfigBuild.getWebClient(webClientKey)
				.method(HttpMethod.valueOf(restFullMethod.name()))
				.uri(uri).headers(toConsumerHttpHeaders(headers))
				.contentType(parseContentType(contentyType, headers))
				.bodyValue(checkBody(requestBody))
				.accept(parseAcceptType(acceptType, headers)).retrieve(), returnType);
		}
		
		if(isForceUniqueDataRestFullResponse || isUniqueDataRestFullResponse(returnType)) {
			return responseParse(UniqueDataWebClientConfigBuild.getWebClient(webClientKey)
				.method(HttpMethod.valueOf(restFullMethod.name()))
				.uri(uri).headers(toConsumerHttpHeaders(headers))
				.accept(parseAcceptType(acceptType, headers)), returnType);
		}
			
		return responseParse(UniqueDataWebClientConfigBuild.getWebClient(webClientKey)
			.method(HttpMethod.valueOf(restFullMethod.name()))
			.uri(uri).headers(toConsumerHttpHeaders(headers))
			.accept(parseAcceptType(acceptType, headers)).retrieve(), returnType);
	}
	
	private Object responseParse(final ResponseSpec webClientResponse, final Type returnType) {
		final GenericReturnTypeClass genericReturnTypeClass = GenericReturnTypeClassHelper.getGenericReturnTypeClass(returnType);
		
		try {
			if(GenericReturnType.PARAMETERIZED_TYPE.equals(genericReturnTypeClass.getReturnType())) {
				if(genericReturnTypeClass.containsType(Map.class)) {
					throw new UniqueDataRestFullException("The Map/HashMap feature is not supported in version 1.0", ExceptionType.REQUEST);
				}
				
				if(genericReturnTypeClass.containsType(Collection.class)) {
					final Class<?> classTypeSubLevel = genericReturnTypeClass.getClassType();
					final Class<?> classTypeSubLevelArrayType = ObjectReflectionHelper.newArrayType(classTypeSubLevel);
					
					if(genericReturnTypeClass.containsType(List.class)) {
						return ObjectReflectionHelper.newInstanceArraList(webClientResponse.bodyToMono(classTypeSubLevelArrayType).block());
					}
					
					// TODO: Implement support for additional collection types and maps in the next version

					LOGGER.error("Cannot convert! Only List.class! The type passed as a parameter: {" + returnType + "} \n "
						+ "		========================================================================================= \n"
						+ "		Support for additional collection types and maps will be available in the next version. \n"
						+ "		=========================================================================================");
					
					throw new UniqueDataRestFullException("Cannot convert! Only List.class! The type passed as a parameter: {" + returnType + "} \n "
						+ "		========================================================================================= \n"
						+ "		Support for additional collection types and maps will be available in the next version. \n"
						+ "		=========================================================================================", ExceptionType.REQUEST);
				}
				
				return webClientResponse.bodyToMono(genericReturnTypeClass.getClassType()).block();
			}

			return webClientResponse.bodyToMono(genericReturnTypeClass.getClassType()).block();	
		}catch (UniqueDataRestFullException uniqueDataRestFullException) {
			LOGGER.error("Error while building request", uniqueDataRestFullException);
			throw uniqueDataRestFullException;
		}catch (WebClientResponseException e) {
			LOGGER.error("Error while building request", e);
			throw new UniqueDataRestFullException(e, ExceptionType.REQUEST);
		}catch (DecodingException e) {
			LOGGER.error("Error while building request", e);
			throw new UniqueDataRestFullException(e.getCause().getMessage(), ExceptionType.REQUEST);
		}catch (Exception e) {
			LOGGER.error("Error while building request", e);
			throw new UniqueDataRestFullException(e.getMessage(), ExceptionType.REQUEST);
		}
	}
	
	private Object responseParse(final RequestHeadersSpec<?> requestHeadersSpec, final Type type) {
		try {
			final AtomicReference<Class<?>> responseBodyType = new AtomicReference<>();
			final GenericReturnTypeClass genericReturnTypeClass = GenericReturnTypeClassHelper.getGenericReturnTypeClass(type);
			
			if(GenericReturnType.DIRECT_CLASS.equals(genericReturnTypeClass.getReturnType())) {
				responseBodyType.set(genericReturnTypeClass.getClassType());
			}
			
			if(GenericReturnType.PARAMETERIZED_TYPE.equals(genericReturnTypeClass.getReturnType())) {
				if(genericReturnTypeClass.containsType(Collection.class)) {
					responseBodyType.set(ObjectReflectionHelper.newArrayType(genericReturnTypeClass.getClassType()));
				}else {
					responseBodyType.set(genericReturnTypeClass.getClassType());
				}
			}
			
			return requestHeadersSpec.exchangeToMono(exchangeToMonoBuild(responseBodyType.get())).block();
		}catch (UniqueDataRestFullException u) {
			LOGGER.error("Error while building request", u);
			throw u;
		}catch (WebClientResponseException e) {
			LOGGER.error("Error while building request", e);
			throw new UniqueDataRestFullException(e, ExceptionType.REQUEST);
		}catch (DecodingException e) {
			LOGGER.error("Error while building request", e);
			throw new UniqueDataRestFullException(e.getCause().getMessage(), ExceptionType.REQUEST);
		}catch (Exception e) {
			LOGGER.error("Error while building request", e);
			throw new UniqueDataRestFullException(e.getMessage(), ExceptionType.REQUEST);
		}	
	}
	
	private <T> Function<ClientResponse, Mono<UniqueDataRestFullResponse<T>>> exchangeToMonoBuild(final Class<T> responseType) {
	    return clientResponse -> clientResponse.bodyToMono(String.class)
	    	.map(requestBody -> toResponse(clientResponse, requestBody, responseType))
	    	.defaultIfEmpty(toResponse(clientResponse));
	}
	
	@SuppressWarnings("unchecked")
	private <T> UniqueDataRestFullResponse<T> toResponse(final ClientResponse clientResponse, 
			final Object requestBody, final Class<T> responseType){
		
		final HttpStatus status = (HttpStatus) clientResponse.statusCode();
		
        final ResponseHttpStatus responseHttpStatus = new ResponseHttpStatus();
        responseHttpStatus.setHttpCode(status.value());
        responseHttpStatus.setHttpStatusMessage(status.getReasonPhrase());
        
        final Map<String, String> responseHeaders = new HashMap<>();
        clientResponse.headers().asHttpHeaders().forEach((key, value) -> {
        	responseHeaders.put(key, value.get(0));
        });
        
        if(UniqueDataJacksonConfigBuild.canParse((String) requestBody, responseType)) {
        	final Object responseObject = UniqueDataJacksonConfigBuild.from((String) requestBody, responseType);
        	return (UniqueDataRestFullResponse<T>) new UniqueDataRestFullResponse<>(responseObject, responseHeaders, responseHttpStatus);
    	}
        
		return (UniqueDataRestFullResponse<T>) new UniqueDataRestFullResponse<String>((String) requestBody, responseHeaders, responseHttpStatus);
	}
	
	private <T> UniqueDataRestFullResponse<T> toResponse(final ClientResponse clientResponse){
		final HttpStatus status = (HttpStatus) clientResponse.statusCode();
		
        final ResponseHttpStatus responseHttpStatus = new ResponseHttpStatus();
        responseHttpStatus.setHttpCode(status.value());
        responseHttpStatus.setHttpStatusMessage(status.getReasonPhrase());
        
        final Map<String, String> responseHeaders = new HashMap<>();
        clientResponse.headers().asHttpHeaders().forEach((key, value) -> {
        	responseHeaders.put(key, value.get(0));
        });
        
		return new UniqueDataRestFullResponse<>(null, responseHeaders, responseHttpStatus);
	}
	
	private Consumer<HttpHeaders> toConsumerHttpHeaders(final Map<String, String> headers){
		final Consumer<HttpHeaders> consumers = consumerHttpHeaders -> {
			if(headers != null) {
				if(headers.containsKey("User-Agent") && headers.containsKey("user-agent")) {
					headers.forEach((key, value) -> consumerHttpHeaders.add(key, value));
				}else {
					headers.put("User-Agent", USER_AGENT);
					headers.forEach((key, value) -> consumerHttpHeaders.add(key, value));
				}
			}
		};
		
		return consumers;
	}
	
	private MediaType parseAcceptType(final RestFullMediaType acceptType, final Map<String, String> headers) {
		if(headers != null && (headers.containsKey("Accept") || headers.containsKey("accept"))) {
			if(!headers.get("Accept").isEmpty()) {
				return MediaType.valueOf(headers.get("Accept"));
			}else{
				return MediaType.valueOf(headers.get("accept"));
			}
		}
		
		return MediaType.valueOf(acceptType.getType());
	}
	
	private MediaType parseContentType(final RestFullMediaType contentyType, final Map<String, String> headers) {
		if(headers != null && (headers.containsKey("Content-Type") || headers.containsKey("content-type"))) {
			if(!headers.get("Content-Type").isEmpty()) {
				return MediaType.valueOf(headers.get("Content-Type"));
			}else{
				return MediaType.valueOf(headers.get("content-type"));
			}
		}
		
		return MediaType.valueOf(contentyType.getType());
	}
	
	private boolean isUniqueDataRestFullResponse(final Type returnType) {
		final GenericReturnTypeClass genericReturnType = GenericReturnTypeClassHelper.getGenericReturnTypeClass(returnType);
		return genericReturnType.getSuperClassType() != null ? genericReturnType.getSuperClassType().equals(UniqueDataRestFullResponse.class) : false;
	}
	
	private boolean isProtocolRequestBody(final RestFullMethod restFullMethod) {
		return RestFullMethod.POST.equals(restFullMethod) || RestFullMethod.PUT.equals(restFullMethod) || RestFullMethod.PATH.equals(restFullMethod);
	}
	
	private Object checkBody(final Object requestBody) {
		return requestBody != null ? requestBody : Void.class;
	}
	
}