package br.com.uniquedata.restfull.sdk.pojo;

import java.time.LocalDateTime;

public class GenericAuthorizeDto {
	
	private static final String BEARER = "Bearer";

	private String bearerToken;
	
	private boolean autoRecover;

	private LocalDateTime expireDate;

	private Class<?> classTypeCredential;
	
	
	public boolean isAvailable() {
		return isExpired() == false;
	}
	
	public boolean isExpired() {
		return LocalDateTime.now().isAfter(expireDate);
	}
	
	public String getBearerToken() {
		return String.format("%s %s", BEARER, bearerToken);
	}
	
	public void setBearerToken(final String bearerToken) {
		this.bearerToken = bearerToken.replaceAll(BEARER, "").trim();
	}
	
	public LocalDateTime getExpireDate() {
		return expireDate;
	}
	
	public void setExpireDate(final LocalDateTime expireDate) {
		this.expireDate = expireDate;
	}
	
	public Class<?> getClassTypeCredential() {
		return classTypeCredential;
	}
	
	public void setClassTypeCredential(final Class<?> classTypeCredential) {
		this.classTypeCredential = classTypeCredential;
	}

	public boolean isAutoRecover() {
		return autoRecover;
	}
	
	public void setAutoRecover(final boolean autoRecover) {
		this.autoRecover = autoRecover;
	}
	
	public GenericAuthorizeDto() {}
	
	public GenericAuthorizeDto(final String bearerToken, final boolean autoRecover, 
		final LocalDateTime expireDate, final Class<?> classTypeCredential) {
		
		this.expireDate = expireDate;
		this.autoRecover = autoRecover;
		this.classTypeCredential = classTypeCredential;
		this.bearerToken = bearerToken.replaceAll(BEARER, "").trim();
	}
	
}