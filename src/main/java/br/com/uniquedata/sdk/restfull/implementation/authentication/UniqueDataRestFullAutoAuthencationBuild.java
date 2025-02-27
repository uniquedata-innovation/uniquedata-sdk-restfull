package br.com.uniquedata.sdk.restfull.implementation.authentication;

public interface UniqueDataRestFullAutoAuthencationBuild {
	
	public static final String AUTHENTICATE = "authenticate";
	public static final String INTERCEPTION = "interception";

	public void authenticate();
	
	public void interception();
	
}
