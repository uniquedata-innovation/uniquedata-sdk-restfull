package br.com.uniquedata.restfull.sdk.implementation.authentication;

public interface UniqueDataRestFullAutoAuthencationBuild {
	
	public static final String AUTHENTICATE = "authenticate";
	public static final String INTERCEPTION = "interception";

	public void authenticate();
	
	public void interception();
	
}
