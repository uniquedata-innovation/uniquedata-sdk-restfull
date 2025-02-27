package br.com.uniquedata.sdk.restfull.pojo;

import java.lang.reflect.Type;
import java.util.Map;

import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

public class UniqueDataRestFullResponse<T> extends Mono<T> {
	
	private T responseBody;

	private ResponseHttpStatus httpStatus;

	private Map<String, String> responseHeaders;

	public ResponseHttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(final ResponseHttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public T getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(final T responseBody) {
		this.responseBody = responseBody;
	}
	
	public Map<String, String> getResponseHeaders() {
		return responseHeaders;
	}

	public void setResponseHeaders(final Map<String, String> responseHeaders) {
		this.responseHeaders = responseHeaders;
	}
	
	@Override
	public void subscribe(final CoreSubscriber<? super T> actual) {}
	
	public UniqueDataRestFullResponse() {}
	
	public UniqueDataRestFullResponse(final T responseBody, 
		final Map<String, String> responseHeaders, final ResponseHttpStatus httpStatus) {
		
		this.httpStatus = httpStatus;
		this.responseBody = responseBody;
		this.responseHeaders = responseHeaders;
	}
	
	public static boolean isEquals(final Type returnType) {
		return UniqueDataRestFullResponse.class.equals(returnType);
	}
	
}
