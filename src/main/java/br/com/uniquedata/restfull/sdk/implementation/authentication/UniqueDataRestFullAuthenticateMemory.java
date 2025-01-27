package br.com.uniquedata.restfull.sdk.implementation.authentication;

import java.util.HashMap;
import java.util.Map;

import br.com.uniquedata.restfull.sdk.exception.UniqueDataRestFullException;
import br.com.uniquedata.restfull.sdk.exception.UniqueDataRestFullException.ExceptionType;

public class UniqueDataRestFullAuthenticateMemory {
	
	private static Map<Class<?>, Object> authenticateAccess;
	
	static {
		authenticateAccess = new HashMap<>();
	}
	
	public static <T> void add(final T object) {
		if(authenticateAccess.containsKey(object.getClass()))
			throw new UniqueDataRestFullException("Error: multiple Credential classes are the same!", ExceptionType.AUTHENTICATION);
		
		authenticateAccess.put(object.getClass(), object);
	}
	
	public static boolean hasNot(final Class<?> classType) {
		return !has(classType);
	}
	
	public static boolean has(final Class<?> classType) {
		return authenticateAccess.containsKey(classType);
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(final Class<?> classType) {
		return (T) classType.cast(authenticateAccess.get(classType));
	}
	
}