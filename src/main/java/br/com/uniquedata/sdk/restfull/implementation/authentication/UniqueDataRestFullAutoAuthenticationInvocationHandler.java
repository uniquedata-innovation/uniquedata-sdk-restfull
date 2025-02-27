package br.com.uniquedata.sdk.restfull.implementation.authentication;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import br.com.uniquedata.sdk.helper.field.FieldReflectionHelper;
import br.com.uniquedata.sdk.helper.pojo.reflectmapper.MapperExtractField;
import br.com.uniquedata.sdk.helper.pojo.reflectmapper.MapperExtractFields;
import br.com.uniquedata.sdk.helper.reflect.mapper.ReflectionMapperHelper;
import br.com.uniquedata.sdk.restfull.annotation.advanced.AdditionalHeader;
import br.com.uniquedata.sdk.restfull.annotation.advanced.Authentication;
import br.com.uniquedata.sdk.restfull.annotation.advanced.AutoAuthentication;
import br.com.uniquedata.sdk.restfull.annotation.advanced.Bearer;
import br.com.uniquedata.sdk.restfull.annotation.advanced.ExpireDate;
import br.com.uniquedata.sdk.restfull.annotation.advanced.Interception;
import br.com.uniquedata.sdk.restfull.annotation.advanced.AutoAuthentication.AuthType;
import br.com.uniquedata.sdk.restfull.annotation.simple.RestFullField;
import br.com.uniquedata.sdk.restfull.annotation.simple.UniqueDataRestFull.RestFullMediaType;
import br.com.uniquedata.sdk.restfull.annotation.simple.UniqueDataRestFull.RestFullMethod;
import br.com.uniquedata.sdk.restfull.exception.UniqueDataRestFullException;
import br.com.uniquedata.sdk.restfull.exception.UniqueDataRestFullException.ExceptionType;
import br.com.uniquedata.sdk.restfull.implementation.clientbuild.UniqueDataJacksonConfigBuild;
import br.com.uniquedata.sdk.restfull.implementation.clientbuild.UniqueDataRestFullWebClientBuild;
import br.com.uniquedata.sdk.restfull.implementation.clientbuild.UniqueDataWebClientConfigBuild;
import br.com.uniquedata.sdk.restfull.pojo.GenericAuthorizeDto;
import br.com.uniquedata.sdk.restfull.pojo.UniqueDataRestFullResponse;

public class UniqueDataRestFullAutoAuthenticationInvocationHandler implements InvocationHandler {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(UniqueDataRestFullAutoAuthenticationInvocationHandler.class);
	
	private static final boolean UNIQUEDATA_RESTFULL_RESPONSE_TYPE = true;
	
	private AutoAuthentication autoAuthentication;
	
	public UniqueDataRestFullAutoAuthenticationInvocationHandler(final AutoAuthentication autoAuthentication) {
		this.autoAuthentication = autoAuthentication;
	}
	
	@Override
	public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
		final Interception interception = autoAuthentication.interception();
		final Authentication authenticate = autoAuthentication.authenticate();
		
		final boolean isAuthTypeBeararToken = AuthType.BEARER_TOKEN.equals(autoAuthentication.type());
		final boolean isInterception = UniqueDataRestFullAutoAuthencationBuild.INTERCEPTION.equals(method.getName());
		final boolean isAuthentication = isAuthTypeBeararToken && UniqueDataRestFullAutoAuthencationBuild.AUTHENTICATE.equals(method.getName());

		if(isAuthentication && authenticate.enabled()) {
			LOGGER.info("[UniqueData] Starting automatic authentication ...");
			authentication(authenticate, interception);
		}
		
		if(isInterception && interception.enabled()) {
			final Class<?> typeClassCredential = authenticate.typeClassCredential();
			final ExchangeFilterFunction exchangeFilterIntercepitor = interception(authenticate, interception);
		
			LOGGER.info("[UniqueData] Creating automatic interception ...");
			UniqueDataWebClientConfigBuild.createWebClientBuild(typeClassCredential, exchangeFilterIntercepitor);
		}
		
		return Void.class;
	}
	
	private ExchangeFilterFunction interception(final Authentication authenticate, final Interception interception) {
		return (request, next) -> {
			final AdditionalHeader[] additionalHeaders = interception.additionalHeaders();
			final Map<String, String> headersMap = toHeadersMap(additionalHeaders);
			request.headers().forEach((key, value) -> headersMap.put(key, value.get(0)));
			
			if(UniqueDataRestFullAuthorizeMemory.isNecessaryAuthenticate(authenticate.typeClassCredential())) {
				if(authenticate.enabled() && isInternalSystemUrl(request, authenticate.fullUrlAuth())) {
					authentication(authenticate, interception);
				}
			}
			
			if(isInternalSystemUrl(request, authenticate.fullUrlAuth()) && !headersMap.containsKey("Authorization")) {
				headersMap.put("Authorization", UniqueDataRestFullAuthorizeMemory.getAuthToken(authenticate.typeClassCredential()));
			}
			
			final Consumer<HttpHeaders> headersConsumer = httpHeaders -> headersMap.forEach(httpHeaders::add);
            headersConsumer.accept(new HttpHeaders());
        	
            return next.exchange(ClientRequest.from(request).headers(headersConsumer).build());
        };
	}
	
	private void authentication(final Authentication authenticate, final Interception interception) {
		try {
			final Map<String, String> headersMap = toHeadersMap(authenticate.additionalHeaders());
			final UniqueDataRestFullWebClientBuild webClientBuild = new UniqueDataRestFullWebClientBuild();
			
			if(authenticate.enabled()) {
				if(UniqueDataRestFullAuthenticateMemory.hasNot(authenticate.typeClassCredential())) {
					UniqueDataRestFullAuthenticateMemory.add(parseRequestBody());
				}
				
				final UniqueDataRestFullResponse<?> responseBody = (UniqueDataRestFullResponse<?>) 
					webClientBuild.build(authenticate.fullUrlAuth(), getRequestBody(headersMap), 
					RestFullMethod.POST, RestFullMediaType.APPLICATION_JSON, RestFullMediaType.APPLICATION_JSON, 
					authenticate.typeClassAuthorize(), authenticate.typeClassCredential(), 
					headersMap, UNIQUEDATA_RESTFULL_RESPONSE_TYPE);
				
				if(responseBody.getHttpStatus().isHttpNotSuccess()) {
					throw new UniqueDataRestFullException(UniqueDataJacksonConfigBuild.toJsonPrettyPrinting(responseBody), ExceptionType.AUTHENTICATION, responseBody);
				}
				
				LOGGER.info("The Auto Authentication process has been initialized successfully!");
				UniqueDataRestFullAuthorizeMemory.addAuthToken(toGenericAuthorize(responseBody.getResponseBody()));
			}
		}catch (UniqueDataRestFullException u) {
			LOGGER.error("Error while building authentication", u);
		}catch (Exception e) {
			LOGGER.error("Error while building authentication", e);
			throw new UniqueDataRestFullException(e, ExceptionType.AUTHENTICATION);
		}
	}
	
	private GenericAuthorizeDto toGenericAuthorize(final Object responseBody) {
		final Authentication authenticate = autoAuthentication.authenticate();
		final Interception interception = autoAuthentication.interception();
		
		final MapperExtractFields mapperExtractFields = ReflectionMapperHelper
			.refletc(responseBody)
			.addScanBy(Bearer.class)
			.addScanBy(ExpireDate.class)
			.toExtractFields();
		
		if(mapperExtractFields.contains(Bearer.class) == false) {
			LOGGER.error("If authentication is enabled, the value of the @Bearer annotation on the typeClassAuthorize class is required.");
			throw new UniqueDataRestFullException("If authentication is enabled, the value of the @Bearer annotation on the typeClassAuthorize class is required.", ExceptionType.AUTHENTICATION);
		}else if(mapperExtractFields.contains(ExpireDate.class) == false && interception.expireInMilliseconds() == -1) {
			LOGGER.error("If authentication is enabled, the value of @ExpireDate annotation on the typeClassAuthorize class is required,"
				+ "Alternatively, you can set the expireInMilliseconds value using @Interception.");
			throw new UniqueDataRestFullException("If authentication is enabled, the value of @ExpireDate annotation on the typeClassAuthorize class is required,"
				+ "Alternatively, you can set the expireInMilliseconds value using @Interception.", ExceptionType.AUTHENTICATION);
		}
		
		final MapperExtractField bearerToken = mapperExtractFields.getUniqueResult(Bearer.class);
		final MapperExtractField expireDate = mapperExtractFields.getUniqueResult(ExpireDate.class);

		final GenericAuthorizeDto genericAuthorize = new GenericAuthorizeDto();
		genericAuthorize.setBearerToken((String) bearerToken.getExtractField().getFieldValue());
		genericAuthorize.setAutoRecover(autoAuthentication.autoRecover());
		genericAuthorize.setClassTypeCredential(authenticate.typeClassCredential());
		
		if(expireDate != null) {
			genericAuthorize.setExpireDate(parseToLocalDateTime(expireDate.getExtractField().getFieldValue(), responseBody.getClass()));
		}else {
			genericAuthorize.setExpireDate(LocalDateTime.now().plus(Duration.ofMillis(interception.expireInMilliseconds())));
		}
		
		return genericAuthorize;
	}
	
	private LocalDateTime parseToLocalDateTime(final Object expireDate, final Class<?> typeClassAuthorize) {
		if(expireDate instanceof Date) {
			return ((Date) expireDate).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		}else if(expireDate instanceof LocalDateTime) {	
			return (LocalDateTime) expireDate;
		}else {
			LOGGER.error("Error: TypeClassAuthorize Object["+typeClassAuthorize+"] need field with "
				+ "java.util.Date or java.time.LocalDateTime and annotation @ExpireDate."
				+ "Alternatively, you can set the expireInMilliseconds value using @Interception.");
			throw new UniqueDataRestFullException("Error: TypeClassAuthorize Object["+typeClassAuthorize+"] need field with "
				+ "java.util.Date or java.time.LocalDateTime and annotation @ExpireDate."
				+ "Alternatively, you can set the expireInMilliseconds value using @Interception.", ExceptionType.AUTHENTICATION);
		}
	}
	
	private Map<String, String> toHeadersMap(final AdditionalHeader[] additionalHeaders){
		final Map<String, String> headers = new HashMap<>();
		
		for (final AdditionalHeader additionalHeader : additionalHeaders) {
			headers.put(additionalHeader.headerName(), additionalHeader.headerValue());
		}
		
		return headers;
	}
	
	private <T> Object parseRequestBody() {
		final Authentication authenticate = autoAuthentication.authenticate();
		final Class<?> typeClassCredential = authenticate.typeClassCredential();
		final String credentialAnntotation = getCredentialAnntotation(authenticate);

		return UniqueDataJacksonConfigBuild.from(credentialAnntotation, typeClassCredential);
	}
	
	private Object getRequestBody(final Map<String, String> headersMap) {
		final Authentication authenticate = autoAuthentication.authenticate();
		final Class<?> typeClassCredential = authenticate.typeClassCredential();
		final Object requestBody = UniqueDataRestFullAuthenticateMemory.get(typeClassCredential);
		
		if(hasContentType(headersMap) && (headersMap.containsValue(MediaType.MULTIPART_FORM_DATA.getType()) 
			&& headersMap.containsValue(MediaType.APPLICATION_FORM_URLENCODED.getType()))) {
			
			return buildRestFullFormDataToObject(FieldReflectionHelper.getFieldNameAndValue(requestBody, RestFullField.class));
		}
		
		return requestBody;
	}
	
	private String getCredentialAnntotation(final Authentication authenticate) {
		if(!authenticate.credentialJsonEnvironmentVariable().isEmpty()) {
			return System.getenv(authenticate.credentialJsonEnvironmentVariable());
		}else if(!authenticate.credentialJsonForTest().isEmpty()) {
			return authenticate.credentialJsonForTest() ;
		}else {
			throw new UniqueDataRestFullException("Body credential is requerid in @Authentication", ExceptionType.AUTHENTICATION);
		}
	}
	
	private String buildRestFullFormDataToObject(final Map<String, Object> parameters) {
		String buildingFormData = new String();
		
		for (final Entry<String, Object> parameter : parameters.entrySet()) {
			if(buildingFormData == null || buildingFormData.isEmpty()) {
				buildingFormData += String.format("%s=%s", parameter.getKey(), parameter.getValue());
			}else{
				buildingFormData += String.format("&%s=%s", parameter.getKey(), parameter.getValue());
			}
		}
		
		return buildingFormData;
	}

	private boolean hasContentType(final Map<String, String> headersMap) {
		return headersMap.containsKey("Content-Type") || headersMap.containsKey("content-type");
	}
	
	private boolean isInternalSystemUrl(final ClientRequest request, final String authUrl) {
		return !request.url().toString().equals(authUrl);
	}
	
}