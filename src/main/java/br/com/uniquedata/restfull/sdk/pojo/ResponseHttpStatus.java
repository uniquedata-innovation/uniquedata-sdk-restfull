package br.com.uniquedata.restfull.sdk.pojo;

import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class ResponseHttpStatus {

	private int httpCode;
	
	private String httpStatusMessage;
	
	public int getHttpCode() {
		return httpCode;
	}
	
	public void setHttpCode(final int httpCode) {
		this.httpCode = httpCode;
	}
	
	public String getHttpStatusMessage() {
		return httpStatusMessage;
	}
	
	public void setHttpStatusMessage(final String httpStatusMessage) {
		this.httpStatusMessage = httpStatusMessage;
	}
	
	public boolean isHttpNotSuccess() {
		return isHttpSuccess() == false;
	}
	
	public boolean isHttpSuccess() {
		return Arrays.asList(200, 201, 202, 203).contains(this.httpCode);
	}
	
	public ResponseHttpStatus() {}

	public ResponseHttpStatus(final int httpCode, final String httpStatusMessage) {
		this.httpCode = httpCode;
		this.httpStatusMessage = httpStatusMessage;
	}
	
	public ResponseHttpStatus(final HttpStatusCode httpStatusCode) {
		this.httpCode = ((HttpStatus) httpStatusCode).value();
		this.httpStatusMessage = ((HttpStatus) httpStatusCode).getReasonPhrase();
	}
	
}