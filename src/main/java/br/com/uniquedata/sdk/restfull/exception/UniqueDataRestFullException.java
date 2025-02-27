package br.com.uniquedata.sdk.restfull.exception;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.web.reactive.function.client.WebClientResponseException;

import br.com.uniquedata.sdk.restfull.pojo.ResponseHttpStatus;
import br.com.uniquedata.sdk.restfull.pojo.UniqueDataRestFullResponse;

public class UniqueDataRestFullException extends RuntimeException {

	private static final long serialVersionUID = -4061446377611113095L;
	
	private String message;
	
	private ExceptionType exceptionType;
	
	private UniqueDataRestFullResponse<?> uniqueDataRestFullResponse;
	
	public UniqueDataRestFullException(final String message, final ExceptionType exceptionType, 
		final UniqueDataRestFullResponse<?> uniqueDataRestFullResponse) {
		super(message);
		
		this.message = message;
		this.exceptionType = exceptionType;
		this.uniqueDataRestFullResponse = uniqueDataRestFullResponse;
	}
	
	public UniqueDataRestFullException(final Throwable throwable, final String message, final ExceptionType exceptionType, 
		final UniqueDataRestFullResponse<?> uniqueDataRestFullResponse) {
		super(message, throwable);
		
		this.message = message;
		this.exceptionType = exceptionType;
		this.uniqueDataRestFullResponse = uniqueDataRestFullResponse;
	}
	
	public UniqueDataRestFullException(final Throwable throwable, final ExceptionType exceptionType,  
			final UniqueDataRestFullResponse<?> uniqueDataRestFullResponse) {
		super(throwable);
		
		this.exceptionType = exceptionType;
		this.message = throwable.getMessage();
		this.uniqueDataRestFullResponse = uniqueDataRestFullResponse;
	}
	
	public UniqueDataRestFullException(final String message, final ExceptionType exceptionType) {
		super(message);
		
		this.message = message;
		this.exceptionType = exceptionType;
	}		
	
	public UniqueDataRestFullException(final Throwable throwable, final ExceptionType exceptionType) {
		super(throwable);

		this.exceptionType = exceptionType;
		this.message = throwable.getMessage();
	}
	
	public UniqueDataRestFullException(final Throwable throwable, final String message, final ExceptionType exceptionType) {
		super(message, throwable);

		this.message = message;
		this.exceptionType = exceptionType;
	}
	
	public UniqueDataRestFullException(final WebClientResponseException exception, final ExceptionType exceptionType) {
		super(exception.getMessage());
		
		this.exceptionType = exceptionType;
		this.message = exception.getMessage();
		
		this.uniqueDataRestFullResponse = new UniqueDataRestFullResponse<>(
			exception.getResponseBodyAsString(), toHeadersWebClientResponse(exception), 
			new ResponseHttpStatus(exception.getStatusCode()));
	}
	
	@Override
	public String getMessage() {
		return message;
	}
	
	public boolean hasHttpResponse() {
		return this.uniqueDataRestFullResponse != null;
	}
	
	public ExceptionType getExceptionType() {
		return exceptionType;
	}
	
	public UniqueDataRestFullResponse<?> getUniqueDataRestFullResponse() {
		return uniqueDataRestFullResponse;
	}
	
	private Map<String, String> toHeadersWebClientResponse(final WebClientResponseException exception){
		final Stream<Entry<String, List<String>>> stream = exception.getHeaders().entrySet().stream();
		return stream.collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().get(0)));
	}
	
	public enum ExceptionType {
		REQUEST, AUTHENTICATION, INTERCEPTION, START_CONFIGURATION 
	}
	
}