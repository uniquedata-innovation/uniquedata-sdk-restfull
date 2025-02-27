package br.com.uniquedata.sdk.restfull.implementation.authentication;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.uniquedata.sdk.restfull.exception.UniqueDataRestFullException;
import br.com.uniquedata.sdk.restfull.exception.UniqueDataRestFullException.ExceptionType;
import br.com.uniquedata.sdk.restfull.implementation.clientbuild.UniqueDataJacksonConfigBuild;
import br.com.uniquedata.sdk.restfull.pojo.GenericAuthorizeDto;

public class UniqueDataRestFullAuthorizeMemory {
	
	/**
	 * Class<?> classTypeCredential @Authentication
	 * 
	 * GenericAuthorizeDto classe gerica para armazenar os dados da resposta de autenticacao
	 * 
	 * */
	private static Map<Class<?>, GenericAuthorizeDto> authTokens;
	
	static {
		authTokens = new HashMap<>();
	}
	
	public static boolean isAutoRecover(final Class<?> classTypeCredential) {
		return authTokens.containsKey(classTypeCredential) ? authTokens.get(classTypeCredential).isAutoRecover() : false;
	}

	public static String getAuthToken(final Class<?> classe) {
		if(authTokens.containsKey(classe) && authTokens.get(classe) != null) {
			return authTokens.get(classe).getBearerToken();
		}
		
		throw new UniqueDataRestFullException("Error: Could not retrieve authorization object!", ExceptionType.AUTHENTICATION);
	}
	
	public static void addAuthToken(final GenericAuthorizeDto genericAuthorize) {
		authTokens.put(genericAuthorize.getClassTypeCredential(), saveDisk(genericAuthorize));
	}
	
	public static boolean isNecessaryAuthenticate(final Class<?> classe) {
		return recover(classe);
	}
	
	private static boolean recover(final Class<?> classTypeCredential) {
		if(!authTokens.containsKey(classTypeCredential) && isAutoRecover(classTypeCredential)) {
			try {
				final String recoverTmpPath = createPath(classTypeCredential);
				final File recoverFile = new File(recoverTmpPath);
				
				if(recoverFile.exists()) {
					final String cacheRecover = FileUtils.readFileToString(recoverFile, "UTF-8");
					final ObjectMapper objectMapper = UniqueDataJacksonConfigBuild.getCustomObjectMapper();
					
					final GenericAuthorizeDto genericAuthorize = objectMapper.readValue(cacheRecover, GenericAuthorizeDto.class);
					
					if(genericAuthorize.isAvailable()) {
						authTokens.put(classTypeCredential, genericAuthorize);
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return hasNotTokenOrExpired(classTypeCredential);
	}
	
	private static GenericAuthorizeDto saveDisk(final GenericAuthorizeDto genericAuthorize) {
		if(genericAuthorize.isAutoRecover()) {
			try {
				final String recoverTmpPath = createPath(genericAuthorize.getClassTypeCredential());
				final ObjectMapper objectMapper = UniqueDataJacksonConfigBuild.getCustomObjectMapper();

				final File recoverFile = new File(recoverTmpPath);
				
				if(recoverFile.exists()) {
					recoverFile.deleteOnExit();
				}
				
				final File recoverFileNew = new File(recoverTmpPath);
				recoverFileNew.setExecutable(true);
				recoverFileNew.setReadable(true);
				recoverFileNew.setWritable(true);
				
				FileUtils.writeByteArrayToFile(recoverFileNew, objectMapper.writeValueAsString(genericAuthorize).getBytes());
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return genericAuthorize;
	}
	
	private static String createPath(final Class<?> typeClasse) {
		final String tmp = System.getProperty("java.io.tmpdir");
		return String.format("%s%s%s.json", tmp, File.separator, typeClasse.getSimpleName());
	}
	
	private static boolean hasNotTokenOrExpired(final Class<?> typeClasse) {
		return !authTokens.containsKey(typeClasse) || authTokens.get(typeClasse).isExpired();
	}
	
}