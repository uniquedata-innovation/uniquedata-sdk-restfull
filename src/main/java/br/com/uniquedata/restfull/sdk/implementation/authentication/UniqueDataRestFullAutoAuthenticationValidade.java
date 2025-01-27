package br.com.uniquedata.restfull.sdk.implementation.authentication;

import br.com.uniquedata.restfull.sdk.annotation.advanced.Authentication;
import br.com.uniquedata.restfull.sdk.annotation.advanced.AutoAuthentication;

public class UniqueDataRestFullAutoAuthenticationValidade {
	
	public Validate validate(final AutoAuthentication authentication) {
		final Authentication authenticate = authentication.authenticate();
		
		if(authenticate.enabled() == true) {
			if(!authenticate.credentialJsonForTest().isEmpty() && !authenticate.credentialJsonEnvironmentVariable().isEmpty()) {
				return new Validate(false, "If authentication is enabled, use either 'credentialJsonEnvironmentVariable' or 'credentialJsonForTest'.");
			}
				
			if(authenticate.credentialJsonForTest().isEmpty() &&
				authenticate.credentialJsonEnvironmentVariable().isEmpty()) {
				
				return new Validate(false, "If authentication is enabled, you must provide a value for either 'credentialJsonEnvironmentVariable' or 'credentialJsonForTest'.");
			}
			
			if(!authenticate.credentialJsonEnvironmentVariable().isEmpty()) {
				final String env = System.getenv(authenticate.credentialJsonEnvironmentVariable());
				
				if(env == null || env.isEmpty()) {
					return new Validate(false, "If authentication is enabled and 'credentialJsonEnvironmentVariable' is chosen, an environment variable name must be provided.");
				}
			}
			
		}
		
		return new Validate(true, null);
	}

	public class Validate {
		
		boolean success;
		
		private String message;

		public Validate() {}
		
		public Validate(final boolean success, final String message) {
			this.success = success;
			this.message = message;
		}
		
		public String getMessage() {
			return message;
		}
		
		public void setMessage(final String message) {
			this.message = message;
		}
		
		public boolean isSuccess() {
			return success;
		}
		
		public void setSuccess(final boolean success) {
			this.success = success;
		}
		
	}
}